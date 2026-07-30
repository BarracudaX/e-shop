package com.barracuda.eshop.customer;

import com.barracuda.eshop.annotation.MySpringBootTest;
import com.barracuda.eshop.customer.dto.CustomerRegistrationForm;
import com.barracuda.eshop.customer.entity.Customer;
import com.barracuda.eshop.customer.exception.DuplicateEmailCustomerException;
import com.barracuda.eshop.customer.repository.CustomerRepository;
import com.barracuda.eshop.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MySpringBootTest
public class CustomerServiceTest extends AbstractCustomerTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final CustomerRegistrationForm johnRegistration = johnRegistrationFormBuilder.build();

    @Test
    void shouldStoreCustomerDataInDatabase() {
        assertThat(customerRepository.findByEmail(johnRegistration.email())).isEmpty();
        Customer expectedCustomer = getExpectedCustomer(johnRegistration);

        customerService.register(johnRegistration);

        assertThat(getCustomerByEmail(johnRegistration.email()))
                .usingRecursiveComparison()
                .ignoringFields("id","password")
                .isEqualTo(expectedCustomer);
    }

    @Test
    void shouldThrowDuplicateEmailWhenRegisteringCustomerWithEmailThatIsAlreadyUsed() {
        customerService.register(johnRegistration);

        assertThatThrownBy(() -> customerService.register(johnRegistration)).isInstanceOf(DuplicateEmailCustomerException.class);
    }

    @Test
    void shouldStoreEncodedCustomerPassword() {
        customerService.register(johnRegistration);
        var storedCustomer = getCustomerByEmail(johnRegistration.email());

        assertThat(storedCustomer.password()).isNotEqualTo(johnRegistration.password());

        assertThat(passwordEncoder.matches(johnRegistration.password(), storedCustomer.password()))
                .withFailMessage("Expected customer password to be encoded with "+passwordEncoder)
                .isTrue();
    }

    private Customer getExpectedCustomer(CustomerRegistrationForm form) {
        return new Customer(0, form.firstName(), form.lastName(), form.email(), form.password());
    }

    private Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).get();
    }
}
