package com.barracuda.eshop.customer.dto;

import com.barracuda.eshop.customer.validation.EqualPasswords;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder(setterPrefix = "with")
@EqualPasswords
public record CustomerRegistrationForm(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Email
        String email,

        String phone,

        @NotNull
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!?#$%^&*()_+])[a-zA-Z0-9!?#$%^&*()_+]{8,24}$")
        String password,

        String repeatedPassword
) {

}
