package com.barracuda.eshop.customer.controller;

import com.barracuda.eshop.customer.dto.CustomerRegistrationForm;
import com.barracuda.eshop.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/customer")
@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    void registerCustomer(@Valid @RequestBody CustomerRegistrationForm registrationForm) {
        customerService.register(registrationForm);
    }

}
