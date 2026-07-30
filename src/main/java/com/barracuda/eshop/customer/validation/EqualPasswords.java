package com.barracuda.eshop.customer.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = EqualPasswordsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface EqualPasswords {

    String message() default "Passwords do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
