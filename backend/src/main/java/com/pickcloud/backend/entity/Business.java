package com.pickcloud.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** Represents a business registered in PickCloud. */
@Entity
@Table(name = "negocio")
@Getter
@Setter
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_negocio")
    private Integer idNegocio;

    // Each business has one administrator and the database allows each administrator
    // to own only one business through the unique id_usuario_admin foreign key.
    @OneToOne
    @JoinColumn(name = "id_usuario_admin", nullable = false, unique = true)
    private User administrador;

    @Column(name = "nombre_comercial", nullable = false, length = 150)
    private String nombreComercial;

    @Column(name = "slug", nullable = false, unique = true, length = 150)
    private String slug;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "stock_minimo_defecto", nullable = false)
    private Integer stockMinimoDefecto;
}
