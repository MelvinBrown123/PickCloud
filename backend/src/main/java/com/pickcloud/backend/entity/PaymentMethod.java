package com.pickcloud.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a payment method available for orders and sales.
 * This entity maps the metodo_pago table from the existing
 * PickCloud database schema.
 */
@Entity
@Table(name = "metodo_pago")
@Getter
@Setter
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Integer idMetodoPago;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;
}