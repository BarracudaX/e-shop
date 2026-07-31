package com.barracuda.eshop.customer;

import com.barracuda.eshop.TestcontainersConfiguration;
import com.barracuda.eshop.customer.repository.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;


@Import({TestcontainersConfiguration.class,CustomerRepository.class})
@JdbcTest
public class CustomerRepositoryTest {
 
    @Autowired
    private CustomerRepository customerRepository;


    @Test
    void shouldReturnEmptyOptionalWhenSearchingForCustomerByEmailThatDoesNotExist() {
        Assertions.assertThat(customerRepository.findByEmail("email_does_not_exist")).isEmpty();
    }
}
