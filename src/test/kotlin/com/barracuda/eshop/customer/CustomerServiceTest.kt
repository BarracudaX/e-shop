package com.barracuda.eshop.customer

import com.barracuda.eshop.annotation.MySpringBootTest
import com.barracuda.eshop.customer.dto.CustomerRegistrationForm
import com.barracuda.eshop.customer.entity.Customer
import com.barracuda.eshop.customer.exception.DuplicateEmailCustomerException
import com.barracuda.eshop.customer.repository.CustomerRepository
import com.barracuda.eshop.customer.service.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

@MySpringBootTest
class CustomerServiceTest : AbstractCustomerTest() {
    @Autowired
    private lateinit var customerService: CustomerService

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    private val johnRegistration: CustomerRegistrationForm = johnRegistrationFormBuilder.build()

    @Test
    fun shouldStoreCustomerDataInDatabase() {
        Assertions.assertThat(customerRepository.findByEmail(johnRegistration.email)).isEmpty()
        val expectedCustomer = getExpectedCustomer(johnRegistration)

        customerService.register(johnRegistration)

        Assertions.assertThat(getCustomerByEmail(johnRegistration.email))
            .usingRecursiveComparison()
            .ignoringFields("id", "password")
            .isEqualTo(expectedCustomer)
    }

    @Test
    fun shouldThrowDuplicateEmailWhenRegisteringCustomerWithEmailThatIsAlreadyUsed() {
        customerService.register(johnRegistration)

        Assertions.assertThatThrownBy { customerService.register(johnRegistration) }
            .isInstanceOf(DuplicateEmailCustomerException::class.java)
    }

    @Test
    fun shouldStoreEncodedCustomerPassword() {
        customerService.register(johnRegistration)
        val storedCustomer = getCustomerByEmail(johnRegistration.email)

        Assertions.assertThat(storedCustomer.password).isNotEqualTo(johnRegistration.password)

        Assertions.assertThat(passwordEncoder.matches(johnRegistration.password, storedCustomer.password))
            .withFailMessage("Expected customer password to be encoded with $passwordEncoder")
            .isTrue()
    }

    private fun getExpectedCustomer(form: CustomerRegistrationForm): Customer {
        return Customer(0, form.firstName, form.lastName, form.email, form.password)
    }

    private fun getCustomerByEmail(email: String): Customer {
        return customerRepository.findByEmail(email).get()
    }
}
