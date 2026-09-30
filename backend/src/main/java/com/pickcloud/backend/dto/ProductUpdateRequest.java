package com.pickcloud.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Represents the editable information of an existing product.
 * Current stock is intentionally excluded because inventory changes
 * must be performed through the dedicated stock operations.
 */
@Getter
@Setter
public class ProductUpdateRequest {

    private Integer idCategoria;
    private Integer idNegocio;

    private String nombre;
    private String descripcion;

    private BigDecimal precioCosto;
    private BigDecimal precioVenta;

    private String sku;
    private Boolean activo;
    private String urlImagen;
    private BigDecimal descuento;
}