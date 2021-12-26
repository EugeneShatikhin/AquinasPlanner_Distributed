package com.aquinas.serviceusers.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class UserDTO {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String bio;
}
