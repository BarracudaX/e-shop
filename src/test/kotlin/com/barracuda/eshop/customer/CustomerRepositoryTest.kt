package com.barracuda.eshop.customer

import com.barracuda.eshop.TestcontainersConfiguration
import com.barracuda.eshop.customer.repository.CustomerRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Import(TestcontainersConfiguration::class, CustomerRepository::class)
@JdbcTest
class CustomerRepositoryTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository


    @Test
    fun shouldReturnEmptyOptionalWhenSearchingForCustomerByEmailThatDoesNotExist() {
        Assertions.assertThat(customerRepository.findByEmail("email_does_not_exist")).isEmpty()
    }
}
