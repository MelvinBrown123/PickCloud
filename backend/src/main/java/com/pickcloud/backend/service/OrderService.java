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
 *
 * Order creation is transactional because creating the order,
 * registering its products, calculating totals, and reserving stock
 * must succeed as one complete operation.
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
     *
     * Prices are obtained directly from the database to prevent clients
     * from manipulating monetary values through the request.
     *
     * Physical stock is not deducted when the order is created.
     * Reserved units are calculated from active Pickup orders.
     *
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

        // Get the current date and the requested pickup date.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pickupDate = request.getFechaRecoleccion();

        // Validate that the pickup date is required.
        if (pickupDate == null) {
            throw new InvalidOperationException(
                    "La fecha de recolección es obligatoria"
            );
        }

        // Validate that the pickup date is in the future.
        if (!pickupDate.isAfter(now)) {
            throw new InvalidOperationException(
                    "La fecha de recolección debe ser futura"
            );
        }

        // Validate that pickup is scheduled for today.
        if (!pickupDate.toLocalDate().equals(now.toLocalDate())) {
            throw new InvalidOperationException(
                    "La recolección debe programarse para el mismo día"
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
        pedido.setEstado(OrderStatus.PENDIENTE.getValue());
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
             * Available stock is calculated by subtracting the units reserved
             * by active Pickup orders from the physical stock.
             */
            Long stockReservado = orderProductRepository
                    .getReservedStockByProduct(producto.getIdProducto());

            int stockDisponible =
                    producto.getStockActual() - stockReservado.intValue();

            if (stockDisponible < item.getCantidad()) {
                throw new InvalidOperationException(
                        "Stock insuficiente. Disponible: " + stockDisponible
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

            /*
             * Saving the order-product relationship represents the reservation.
             * Physical stock remains unchanged at this point.
             */
            orderProductRepository.save(pedidoProducto);

            total = total.add(subtotal);
        }

        pedido.setTotal(total);
        pedido = orderRepository.save(pedido);

        return toResponse(pedido);
    }

    /**
     * Cancels a pending order and releases its stock reservation.
     *
     * Physical stock is not modified because the reservation is represented
     * by the quantities associated with active Pickup orders.
     */
    @Transactional
    public OrderResponse cancelOrder(Integer idPedido) {

        Order pedido = orderRepository.findById(idPedido)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pedido no encontrado"));

        if (!OrderStatus.PENDIENTE.getValue()
                .equalsIgnoreCase(pedido.getEstado())) {

            throw new InvalidOperationException(
                    "Solo se pueden cancelar pedidos pendientes"
            );
        }

        /*
         * Changing the status to cancelled automatically releases the
         * reservation because cancelled orders are not counted as reserved stock.
         */
        pedido.setEstado(OrderStatus.CANCELADO.getValue());

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
     * Confirms the online payment of a pending order.
     *
     * Online payments are processed through the configured payment gateway.
     * Once payment is confirmed, reserved units become a definitive
     * inventory deduction.
     */
    @Transactional
    public OrderResponse payOrder(Integer idPedido) {

        Order pedido = orderRepository.findById(idPedido)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pedido no encontrado"));

        if (!OrderStatus.PENDIENTE.getValue()
                .equalsIgnoreCase(pedido.getEstado())) {

            throw new InvalidOperationException(
                    "Solo se pueden pagar pedidos pendientes"
            );
        }

        if (!"PAGO_EN_LINEA".equalsIgnoreCase(
                pedido.getMetodoPago().getDescripcion())) {

            throw new InvalidOperationException(
                    "Este pedido no utiliza pago en línea"
            );
        }

        /*
         * Online payment converts the temporary reservation into a
         * definitive inventory deduction.
         */
        deductOrderStock(pedido);

        pedido.setEstado(OrderStatus.PAGADO.getValue());

        pedido = orderRepository.save(pedido);

        return toResponse(pedido);
    }


    /**
     * Marks an order as ready for pickup.
     *
     * Online orders must already be paid.
     * Orders paid at the store can transition directly from pending to ready.
     */
    @Transactional
    public OrderResponse readyOrder(Integer idPedido) {

        Order pedido = orderRepository.findById(idPedido)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pedido no encontrado"));

        boolean pagoEnLinea = "PAGO_EN_LINEA".equalsIgnoreCase(
                pedido.getMetodoPago().getDescripcion()
        );

        if (pagoEnLinea) {

            if (!OrderStatus.PAGADO.getValue()
                    .equalsIgnoreCase(pedido.getEstado())) {

                throw new InvalidOperationException(
                        "Los pedidos con pago en línea deben estar pagados antes de prepararse"
                );
            }

        } else {

            if (!OrderStatus.PENDIENTE.getValue()
                    .equalsIgnoreCase(pedido.getEstado())) {

                throw new InvalidOperationException(
                        "Solo se pueden preparar pedidos pendientes"
                );
            }
        }

        pedido.setEstado(OrderStatus.LISTO.getValue());

        pedido = orderRepository.save(pedido);

        return toResponse(pedido);
    }

    /**
     * Marks an order that is ready for pickup as delivered.
     *
     * For orders paid at the store, inventory is definitively deducted
     * when the order is delivered. Online orders were already deducted
     * when their payment was confirmed.
     */
    @Transactional
    public OrderResponse deliverOrder(Integer idPedido) {

        Order pedido = orderRepository.findById(idPedido)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pedido no encontrado"));

        if (!OrderStatus.LISTO.getValue()
                .equalsIgnoreCase(pedido.getEstado())) {

            throw new InvalidOperationException(
                    "Solo se pueden entregar pedidos listos"
            );
        }

        boolean pagoEnLinea = "PAGO_EN_LINEA".equalsIgnoreCase(
                pedido.getMetodoPago().getDescripcion()
        );

        /*
         * Store payments keep their stock reserved until delivery.
         * Online payments were already deducted when payment was confirmed.
         */
        if (!pagoEnLinea) {
            deductOrderStock(pedido);
        }

        pedido.setEstado(OrderStatus.ENTREGADO.getValue());

        pedido = orderRepository.save(pedido);

        return toResponse(pedido);
    }

    /**
     * Definitively deducts the products associated with an order
     * from the physical inventory.
     */
    private void deductOrderStock(Order pedido) {

        List<OrderProduct> productosPedido =
                orderProductRepository.findByPedido_IdPedido(
                        pedido.getIdPedido()
                );

        for (OrderProduct pedidoProducto : productosPedido) {

            Product producto = pedidoProducto.getProducto();

            if (producto.getStockActual() < pedidoProducto.getCantidad()) {
                throw new InvalidOperationException(
                        "Stock físico insuficiente para completar el pedido"
                );
            }

            producto.setStockActual(
                    producto.getStockActual()
                            - pedidoProducto.getCantidad()
            );

            productRepository.save(producto);
        }
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