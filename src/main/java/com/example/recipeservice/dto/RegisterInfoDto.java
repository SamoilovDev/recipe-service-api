package com.example.recipeservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterInfoDto {

    @NotNull(message = "email is required!")
    @Email(regexp = "^[-.\\w]+@\\w+\\.\\w+$", message = "Email is not valid!")
    private String email;

    @NotNull(message = "password is required!")
    @Pattern(regexp = "^[-.:\\w]{8,}$", message = "password is not valid!")
    private String password;

}
