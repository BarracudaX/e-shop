package com.barracuda.eshop.customer;

import com.barracuda.eshop.annotation.MyMvcTest;
import com.barracuda.eshop.customer.dto.CustomerRegistrationForm;
import com.barracuda.eshop.customer.exception.DuplicateEmailCustomerException;
import com.barracuda.eshop.customer.service.CustomerService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.assertj.WebTestClientResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@MyMvcTest
public class CustomerRegistrationTest extends AbstractCustomerTest {

    @MockitoBean
    private CustomerService customerService;

    @Autowired private MockMvc mockMvc;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        webTestClient = MockMvcWebTestClient.bindTo(mockMvc).build();
    }

    @Test
    void customerShouldBeAbleToRegister() {
        assertThat(sendRegistrationRequest(johnRegistrationFormBuilder.build())).hasStatus2xxSuccessful();
    }

    @Test
    void shouldNotAllowRegisteringCustomerWithEmailThatIsAlreadyInUseByAnotherCustomer() {
        doNothing().doThrow(DuplicateEmailCustomerException.class).when(customerService).register(any(CustomerRegistrationForm.class));

        sendRegistrationRequest(johnRegistrationFormBuilder.build());

        assertThat(sendRegistrationRequest(johnRegistrationFormBuilder.build())).hasStatus(HttpStatus.CONFLICT);
    }


    @MethodSource("blankStrings")
    @ParameterizedTest
    void shouldNotAllowRegisteringCustomerWithEmptyFirstName(String blankFirstName) {
        var result = sendRegistrationRequest(johnRegistrationFormBuilder.withFirstName(blankFirstName).build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @MethodSource("blankStrings")
    @ParameterizedTest
    void shouldNotAllowRegisteringCustomerWithEmptyLastName(String blankLastName) {
        var result = sendRegistrationRequest(johnRegistrationFormBuilder.withLastName(blankLastName).build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @MethodSource("invalidEmails")
    @ParameterizedTest
    void shouldNotAllowRegisteringCustomerWithInvalidEmail() {
        var result = sendRegistrationRequest(johnRegistrationFormBuilder.withEmail("invalid_email").build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @MethodSource("shortPasswords")
    @ParameterizedTest
    void shouldNotAllowRegisteringCustomerWithPasswordLengthLessThan8Characters(String shortPassword) {
        var result = sendRegistrationRequest(johnRegistrationFormBuilder.withPassword(shortPassword).build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotAllowRegisteringCustomerWithoutPassword() {
        var result = sendRegistrationRequest(johnRegistrationFormBuilder.withPassword(null).build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @MethodSource("onlyDigitPasswords")
    @ParameterizedTest
    void shouldNotAllowRegisterCustomerWithPasswordThatDoesNotContainLetters(String onlyDigitPassword) {
        var result = sendRegistrationRequest(johnRegistrationFormBuilder.withPassword(onlyDigitPassword).build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotAllowRegisteringCustomerWithPasswordThatDoesNotContainAtLeatOneSpecialCharacter() {
        var result = sendRegistrationRequest(johnRegistrationFormBuilder.withPassword("Password123").build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @MethodSource("passwordSpecialCharacters")
    @ParameterizedTest
    void successfulCustomerRegistrationWithPasswordContainingSpecialCharacter(String specialCharacter) {
        String password = "Password123" + specialCharacter;
        var result = sendRegistrationRequest(johnRegistrationFormBuilder.withPassword(password).withRepeatedPassword(password).build());

        assertThat(result).hasStatus(HttpStatus.OK);
    }

    @Test
    void shouldNotAllowRegisteringCustomerWhenTwoPasswordsDoNotMatch() {
        var result = sendRegistrationRequest(johnRegistrationFormBuilder.withPassword("Password123!").withRepeatedPassword("Password123").build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    private @NonNull WebTestClientResponse sendRegistrationRequest(CustomerRegistrationForm registrationForm) {
        return WebTestClientResponse.from(webTestClient
                .post()
                .uri("/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationForm)
                .exchange()
        );
    }


}
