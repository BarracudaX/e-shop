package com.barracuda.eshop.customer.validation;

import com.barracuda.eshop.customer.dto.CustomerRegistrationForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EqualPasswordsValidator implements ConstraintValidator<EqualPasswords, CustomerRegistrationForm> {

    @Override
    public boolean isValid(CustomerRegistrationForm value, ConstraintValidatorContext context) {
        if (value.password() == null) {
            return true;
        }

        return value.password().equals(value.repeatedPassword());
    }
}
