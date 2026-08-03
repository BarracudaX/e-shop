package com.barracuda.eshop.customer.repository;

import com.barracuda.eshop.customer.entity.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Customer save(Customer customer);
}
