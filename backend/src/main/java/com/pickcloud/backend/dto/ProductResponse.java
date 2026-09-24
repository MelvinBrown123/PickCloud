package com.pickcloud.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Data Transfer Object returned by product endpoints.
 *
 * It flattens the Product entity and its relationships into values that are useful
 * to API clients, avoiding serialization of complete Category and Business entities.
 */
@Getter
@Setter
public class ProductResponse {
    private Integer idProducto;
    private Integer idCategoria;
    private String categoria;
    private Integer idNegocio;
    private String nombre;
    private String descripcion;
    private BigDecimal precioCosto;
    private BigDecimal precioVenta;
    private Integer stockActual;
    private String sku;
    private Boolean activo;
    private String urlImagen;
    private BigDecimal descuento;
}
