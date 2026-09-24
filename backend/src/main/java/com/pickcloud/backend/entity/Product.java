package com.pickcloud.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/** Represents a product in the inventory of a PickCloud business. */
@Entity
@Table(
        name = "producto",
        uniqueConstraints = {
                // SKU uniqueness is scoped to a business, so different businesses
                // may use the same SKU while one business cannot duplicate it.
                @UniqueConstraint(
                        name = "uq_producto_negocio_sku",
                        columnNames = {"id_negocio", "sku"}
                )
        }
)
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    // Many products can belong to the same category.
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Category categoria;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // BigDecimal is used for monetary values to avoid floating-point precision issues.
    @Column(name = "precio_costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCosto;

    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @Column(name = "sku", nullable = false, length = 80)
    private String sku;

    // Allows logical activation/deactivation without deleting the database record.
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "url_imagen", length = 500)
    private String urlImagen;

    @Column(name = "descuento", nullable = false, precision = 5, scale = 2)
    private BigDecimal descuento;

    // The direct business relationship makes each product explicitly tenant-scoped.
    @ManyToOne
    @JoinColumn(name = "id_negocio", nullable = false)
    private Business negocio;
}
