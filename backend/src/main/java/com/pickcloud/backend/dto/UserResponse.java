package com.pickcloud.backend.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Safe representation of a user returned by the API.
 *
 * The password stored by the User entity is intentionally omitted so it cannot be
 * serialized in responses sent to clients.
 */
@Getter
@Setter
public class UserResponse {
    private Integer idUsuario;
    private Integer idRol;
    private String rol;
    private String nombre;
    private String correo;
}
