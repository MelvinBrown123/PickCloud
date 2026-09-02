package com.pickcloud.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private boolean active;
}