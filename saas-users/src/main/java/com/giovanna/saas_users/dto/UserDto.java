package com.giovanna.saas_users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(@NotBlank String name, @NotBlank @Email String email) {
}
