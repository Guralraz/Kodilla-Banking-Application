package com.greg.banking_app.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateDto {
    private String peselNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
}
