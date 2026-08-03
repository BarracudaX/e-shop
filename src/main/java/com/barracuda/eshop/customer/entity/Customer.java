package com.barracuda.eshop.customer.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.crypto.password.PasswordEncoder;

@Table("customers")
public record Customer(

        @Id
        long id,

        String firstName,

        String lastName,

        String email,

        String password
) {

    public Customer withEncodedPassword(PasswordEncoder passwordEncoder) {
        return new Customer(id, firstName, lastName, email, passwordEncoder.encode(password));
    }

}
