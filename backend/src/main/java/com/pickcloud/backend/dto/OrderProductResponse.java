package com.pickcloud.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Represents one product included in an order response.
 */
@Getter
@Setter
public class OrderProductResponse {

    private Integer idProducto;
    private String nombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}