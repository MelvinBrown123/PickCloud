package com.pickcloud.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
@Getter
@Setter
public class ProductRequest {

    private Integer idCategoria;
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