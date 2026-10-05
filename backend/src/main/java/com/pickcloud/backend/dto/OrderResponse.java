package com.pickcloud.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the complete order information returned by the API.
 */
@Getter
@Setter
public class OrderResponse {

    private Integer idPedido;
    private Integer idUsuario;
    private Integer idNegocio;
    private Integer idMetodoPago;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaRecoleccion;
    private BigDecimal total;
    private List<OrderProductResponse> productos;
}