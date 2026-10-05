package com.pickcloud.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the information required to create a new Pickup order.
 * Monetary values are intentionally excluded because totals must be
 * calculated by the backend using trusted product prices from the database.
 */
@Getter
@Setter
public class OrderRequest {

    private Integer idUsuario;
    private Integer idNegocio;
    private Integer idMetodoPago;
    private LocalDateTime fechaRecoleccion;
    private List<OrderItemRequest> productos;
}