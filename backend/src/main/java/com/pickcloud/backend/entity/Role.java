package com.pickcloud.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** Maps the Java Role entity to the existing Spanish database table named rol. */
@Entity
@Table(name = "rol")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;
}
