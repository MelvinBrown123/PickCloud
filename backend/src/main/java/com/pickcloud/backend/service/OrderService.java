package com.pickcloud.backend.service;

import com.pickcloud.backend.dto.OrderItemRequest;
import com.pickcloud.backend.dto.OrderProductResponse;
import com.pickcloud.backend.dto.OrderRequest;
import com.pickcloud.backend.dto.OrderResponse;
import com.pickcloud.backend.entity.*;
import com.pickcloud.backend.exception.InvalidOperationException;
import com.pickcloud.backend.exception.ResourceNotFoundException;
import com.pickcloud.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the business logic for Pickup orders.
 * Order creation is transactional because creating the order, registering
 * its products, calculating totals, and reserving stock must succeed as
 * one complete operation.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderProductRepository orderProductRepository,
            UserRepository userRepository,
            BusinessRepository businessRepository,
            PaymentMethodRepository paymentMethodRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.productRepository = productRepository;
    }

    /**
     * Creates a Pickup order and reserves the requested inventory.

     * Prices are obtained directly from the database to prevent clients
     * from manipulating monetary values through the request.

     * @Transactional guarantees that if any validation or database operation
     * fails, all changes performed during this method are rolled back.
     */
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        User usuario = userRepository.findById(request.getIdUsuario())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));

        Business negocio = businessRepository.findById(request.getIdNegocio())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Negocio no encontrado"));

        PaymentMethod metodoPago = paymentMethodRepository
                .findById(request.getIdMetodoPago())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Método de pago no encontrado"));

        if (request.getProductos() == null || request.getProductos().isEmpty()) {
            throw new InvalidOperationException(
                    "El pedido debe contener al menos un producto"
            );
        }

        /*
         * The order is initially created with a zero total.
         * The final amount is calculated from the products stored in PostgreSQL.
         */
        Order pedido = new Order();
        pedido.setUsuario(usuario);
        pedido.setNegocio(negocio);
        pedido.setMetodoPago(metodoPago);
        pedido.setEstado("pendiente");
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setFechaRecoleccion(request.getFechaRecoleccion());
        pedido.setTotal(BigDecimal.ZERO);

        pedido = orderRepository.save(pedido);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest item : request.getProductos()) {

            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new InvalidOperationException(
                        "La cantidad de cada producto debe ser mayor a cero"
                );
            }

            Product producto = productRepository.findById(item.getIdProducto())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Producto no encontrado"));

            /*
             * A Pickup order may only contain products belonging to
             * the business where the order will be collected.
             */
            if (!producto.getNegocio().getIdNegocio()
                    .equals(negocio.getIdNegocio())) {

                throw new InvalidOperationException(
                        "El producto no pertenece al negocio del pedido"
                );
            }

            if (!producto.getActivo()) {
                throw new InvalidOperationException(
                        "El producto no está activo"
                );
            }

            /*
             * Stock is validated before reserving the requested units.
             * The transaction will roll back previous changes if any later
             * product fails validation.
             */
            if (producto.getStockActual() < item.getCantidad()) {
                throw new InvalidOperationException(
                        "Stock insuficiente para el producto: "
                                + producto.getNombre()
                );
            }

            BigDecimal precioUnitario = producto.getPrecioVenta();

            BigDecimal subtotal = precioUnitario.multiply(
                    BigDecimal.valueOf(item.getCantidad())
            );

            OrderProduct pedidoProducto = new OrderProduct();
            pedidoProducto.setPedido(pedido);
            pedidoProducto.setProducto(producto);
            pedidoProducto.setCantidad(item.getCantidad());
            pedidoProducto.setPrecioUnitario(precioUnitario);
            pedidoProducto.setSubtotal(subtotal);

            orderProductRepository.save(pedidoProducto);

            /*
             * Reserving inventory prevents the same units from being sold
             * while this Pickup order is pending.
             */
            producto.setStockActual(
                    producto.getStockActual() - item.getCantidad()
            );

            productRepository.save(producto);

            total = total.add(subtotal);
        }

        pedido.setTotal(total);
        pedido = orderRepository.save(pedido);

        return toResponse(pedido);
    }

    /**
     * Cancels a pending order and releases all inventory reserved by it.

     * The operation is transactional to ensure that both the order status
     * and all stock quantities are updated as one atomic operation.
     */
    @Transactional
    public OrderResponse cancelOrder(Integer idPedido) {

        Order pedido = orderRepository.findById(idPedido)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pedido no encontrado"));

        /*
         * Only pending orders can be cancelled.
         * This prevents inventory from being restored multiple times.
         */
        if (!"pendiente".equalsIgnoreCase(pedido.getEstado())) {
            throw new InvalidOperationException("Solo se pueden cancelar pedidos pendientes");
        }

        List<OrderProduct> productosPedido =
                orderProductRepository.findByPedido_IdPedido(idPedido);

        /*
         * Release every quantity previously reserved when the order
         * was created.
         */
        for (OrderProduct pedidoProducto : productosPedido) {

            Product producto = pedidoProducto.getProducto();

            producto.setStockActual(
                    producto.getStockActual()
                            + pedidoProducto.getCantidad()
            );

            productRepository.save(producto);
        }

        pedido.setEstado("cancelado");

        pedido = orderRepository.save(pedido);

        return toResponse(pedido);
    }

    /**
     * Retrieves all orders associated with a specific business.
     */
    public List<OrderResponse> getOrdersByBusiness(Integer idNegocio) {

        if (!businessRepository.existsById(idNegocio)) {
            throw new ResourceNotFoundException("Negocio no encontrado");
        }

        return orderRepository.findByNegocio_IdNegocio(idNegocio)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Marks a pending Pickup order as delivered.

     * Inventory is not modified because the requested units were already
     * reserved when the order was created.
     */
    @Transactional
    public OrderResponse deliverOrder(Integer idPedido) {

        Order pedido = orderRepository.findById(idPedido)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pedido no encontrado"));

        /*
         * Only pending orders can be delivered.
         * Cancelled or previously delivered orders cannot transition
         * to the delivered state.
         */
        if (!"pendiente".equalsIgnoreCase(pedido.getEstado())) {
            throw new InvalidOperationException(
                    "Solo se pueden entregar pedidos pendientes"
            );
        }

        pedido.setEstado("entregado");

        pedido = orderRepository.save(pedido);

        return toResponse(pedido);
    }

    /**
     * Retrieves an order by its identifier.
     *
     * The response includes the order information and all products
     * associated with it.
     */
    public OrderResponse getOrder(Integer idPedido) {

        Order pedido = orderRepository.findById(idPedido)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pedido no encontrado"));

        return toResponse(pedido);
    }

    /**
     * Converts an Order entity and its associated products into the DTO
     * exposed by the REST API.
     */
    private OrderResponse toResponse(Order pedido) {

        OrderResponse response = new OrderResponse();

        response.setIdPedido(pedido.getIdPedido());
        response.setIdUsuario(pedido.getUsuario().getIdUsuario());
        response.setIdNegocio(pedido.getNegocio().getIdNegocio());
        response.setIdMetodoPago(
                pedido.getMetodoPago().getIdMetodoPago()
        );

        response.setEstado(pedido.getEstado());
        response.setFechaCreacion(pedido.getFechaCreacion());
        response.setFechaRecoleccion(pedido.getFechaRecoleccion());
        response.setTotal(pedido.getTotal());

        List<OrderProductResponse> productos = new ArrayList<>();

        for (OrderProduct pedidoProducto :
                orderProductRepository.findByPedido_IdPedido(
                        pedido.getIdPedido())) {

            OrderProductResponse productoResponse =
                    new OrderProductResponse();

            productoResponse.setIdProducto(
                    pedidoProducto.getProducto().getIdProducto()
            );

            productoResponse.setNombre(
                    pedidoProducto.getProducto().getNombre()
            );

            productoResponse.setCantidad(
                    pedidoProducto.getCantidad()
            );

            productoResponse.setPrecioUnitario(
                    pedidoProducto.getPrecioUnitario()
            );

            productoResponse.setSubtotal(
                    pedidoProducto.getSubtotal()
            );

            productos.add(productoResponse);
        }

        response.setProductos(productos);

        return response;
    }
}