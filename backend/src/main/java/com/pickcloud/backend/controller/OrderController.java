package com.pickcloud.backend.controller;

import com.pickcloud.backend.dto.OrderRequest;
import com.pickcloud.backend.dto.OrderResponse;
import com.pickcloud.backend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Exposes REST endpoints related to Pickup orders.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new Pickup order and reserves its inventory.
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request) {

        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Cancels a pending pickup order and releases its reserved inventory.
     * */

    @PatchMapping("/{idPedido}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Integer idPedido) {
        return ResponseEntity.ok(
                orderService.cancelOrder(idPedido)
        );
    }

    /**
     * Retrieves all Pickup orders associated with a business.
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrdersByBusiness(
            @RequestParam Integer idNegocio) {

        return ResponseEntity.ok(
                orderService.getOrdersByBusiness(idNegocio)
        );
    }

    /**
     * Marks a pending Pickup order as delivered.
     */
    @PatchMapping("/{idPedido}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(
            @PathVariable Integer idPedido) {

        return ResponseEntity.ok(
                orderService.deliverOrder(idPedido)
        );
    }

    /**
     * Retrieves a specific Pickup order by its identifier.
     */
    @GetMapping("/{idPedido}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Integer idPedido) {

        return ResponseEntity.ok(
                orderService.getOrder(idPedido)
        );
    }

    /**
     * Marks an order as paid.
     */
    @PatchMapping("/{idPedido}/pay")
    public ResponseEntity<OrderResponse> payOrder(
            @PathVariable Integer idPedido) {

        return ResponseEntity.ok(
                orderService.payOrder(idPedido)
        );
    }

    /**
     * Marks an order as ready for pickup.
     */
    @PatchMapping("/{idPedido}/ready")
    public ResponseEntity<OrderResponse> readyOrder(
            @PathVariable Integer idPedido) {

        return ResponseEntity.ok(
                orderService.readyOrder(idPedido)
        );
    }
}