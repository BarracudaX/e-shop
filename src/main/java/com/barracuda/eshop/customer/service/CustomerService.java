package com.barracuda.eshop.customer.service;


import com.barracuda.eshop.customer.dto.CustomerRegistrationForm;
import com.barracuda.eshop.customer.entity.Customer;
import com.barracuda.eshop.customer.mapper.CustomerMapper;
import com.barracuda.eshop.customer.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(CustomerRegistrationForm registrationForm) {
        Customer customer = CustomerMapper.INSTANCE.customerRegistrationFormToCustomer(registrationForm).withEncodedPassword(passwordEncoder);
        customerRepository.insertCustomer(customer);
    }

}
