package com.barracuda.eshop.customer.entity;


import org.springframework.security.crypto.password.PasswordEncoder;

public record Customer(long id, String firstName, String lastName, String email, String password) {

    public Customer withEncodedPassword(PasswordEncoder passwordEncoder) {
        return new Customer(id, firstName, lastName, email, passwordEncoder.encode(password));
    }

}
