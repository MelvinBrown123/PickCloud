package com.pickcloud.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a customer order created for a specific business.
 * An order stores its current state, creation and pickup dates,
 * payment method, customer, business, and calculated total.
 */
@Entity
@Table(name = "pedido")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    private PaymentMethod metodoPago;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_recoleccion")
    private LocalDateTime fechaRecoleccion;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "id_negocio", nullable = false)
    private Business negocio;
}