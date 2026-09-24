package com.pickcloud.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** Represents a product category that belongs to a specific business. */
@Entity
@Table(
        name = "categoria",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_categoria_negocio_descripcion",
                        columnNames = {"id_negocio", "descripcion"}
                )
        }
)
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;

    // Multiple categories can belong to the same business.
    @ManyToOne
    @JoinColumn(name = "id_negocio", nullable = false)
    private Business negocio;
}
