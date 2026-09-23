package com.pickcloud.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Integer idUsuario;
    private Integer idRol;
    private String rol;
    private String nombre;
    private String correo;
}