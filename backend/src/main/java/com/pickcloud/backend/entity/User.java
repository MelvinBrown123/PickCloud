package com.pickcloud.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** Represents a PickCloud user stored in the usuarios table. */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    // Many users can share the same role. id_rol is the foreign key in usuarios.
    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Role rol;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String correo;

    // Stored internally and intentionally excluded from UserResponse.
    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;
}
