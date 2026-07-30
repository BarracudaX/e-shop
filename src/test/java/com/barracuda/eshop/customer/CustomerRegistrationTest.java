package com.barracuda.eshop.customer;

import com.barracuda.eshop.annotation.MyMvcTest;
import com.barracuda.eshop.customer.dto.CustomerRegistrationForm;
import com.barracuda.eshop.customer.dto.CustomerRegistrationForm.CustomerRegistrationFormBuilder;
import com.barracuda.eshop.customer.exception.DuplicateEmailCustomerException;
import com.barracuda.eshop.customer.service.CustomerService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@MyMvcTest
public class CustomerRegistrationTest {

    @MockitoBean
    private CustomerService customerService;

    @Autowired private MockMvcTester mockMvc;

    @Autowired private ObjectMapper objectMapper;
    private final CustomerRegistrationFormBuilder validRegistrationForm = CustomerRegistrationForm
            .builder()
            .withFirstName("John")
            .withLastName("Doe")
            .withEmail("test@email.com")
            .withPhone("123456789")
            .withPassword("ValidPass123!")
            .withRepeatedPassword("ValidPass123!");

    @Test
    void customerShouldBeAbleToRegister() {
        var result = sendRegistrationRequest(validRegistrationForm.build());

        assertThat(result).hasStatus(HttpStatus.OK);
    }

    @Test
    void shouldNotAllowRegisteringCustomerWithEmailThatIsAlreadyInUseByAnotherCustomer() {
        doNothing().doThrow(DuplicateEmailCustomerException.class).when(customerService).register(any(CustomerRegistrationForm.class));
        sendRegistrationRequest(validRegistrationForm.build());

        var result = sendRegistrationRequest(validRegistrationForm.build());

        assertThat(result).hasStatus(HttpStatus.CONFLICT);
    }


    @MethodSource("blankStrings")
    @ParameterizedTest
    void shouldNotAllowRegisteringCustomerWithEmptyFirstName(String blankFirstName) {
        var result = sendRegistrationRequest(validRegistrationForm.withFirstName(blankFirstName).build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @MethodSource("blankStrings")
    @ParameterizedTest
    void shouldNotAllowRegisteringCustomerWithEmptyLastName(String blankLastName) {
        var result = sendRegistrationRequest(validRegistrationForm.withLastName(blankLastName).build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @MethodSource("invalidEmails")
    @ParameterizedTest
    void shouldNotAllowRegisteringCustomerWithInvalidEmail() {
        var result = sendRegistrationRequest(validRegistrationForm.withEmail("invalid_email").build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @MethodSource("shortPasswords")
    @ParameterizedTest
    void shouldNotAllowRegisteringCustomerWithPasswordLengthLessThan8Characters(String shortPassword) {
        var result = sendRegistrationRequest(validRegistrationForm.withPassword(shortPassword).build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotAllowRegisteringCustomerWithoutPassword() {
        var result = sendRegistrationRequest(validRegistrationForm.withPassword(null).build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @MethodSource("onlyDigitPasswords")
    @ParameterizedTest
    void shouldNotAllowRegisterCustomerWithPasswordThatDoesNotContainLetters(String onlyDigitPassword) {
        var result = sendRegistrationRequest(validRegistrationForm.withPassword(onlyDigitPassword).build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotAllowRegisteringCustomerWithPasswordThatDoesNotContainAtLeatOneSpecialCharacter() {
        var result = sendRegistrationRequest(validRegistrationForm.withPassword("Password123").build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @MethodSource("passwordSpecialCharacters")
    @ParameterizedTest
    void successfulCustomerRegistrationWithPasswordContainingSpecialCharacter(String specialCharacter) {
        String password = "Password123" + specialCharacter;
        var result = sendRegistrationRequest(validRegistrationForm.withPassword(password).withRepeatedPassword(password).build());

        assertThat(result).hasStatus(HttpStatus.OK);
    }

    @Test
    void shouldNotAllowRegisteringCustomerWhenTwoPasswordsDoNotMatch() {
        var result = sendRegistrationRequest(validRegistrationForm.withPassword("Password123!").withRepeatedPassword("Password123").build());

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    private @NonNull MvcTestResult sendRegistrationRequest(CustomerRegistrationForm registrationForm) {
        return mockMvc
                .post().uri("/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationForm))
                .exchange();
    }

    private static List<String> passwordSpecialCharacters(){
        return List.of("!","?","#","$","%","^","&","*","(",")","_","+");
    }

    private static List<String> onlyDigitPasswords(){
        return List.of("12345678", "1039012581", "108990835", "3587895790139");
    }

    private static List<String> shortPasswords(){
        return List.of(
                "","1","12","123","1234","passw","123456","1234567"
        );
    }

    private static List<String> blankStrings() {
        var blankStrings = new ArrayList<String>();

        blankStrings.add("");
        blankStrings.add("   ");
        blankStrings.add("   ");
        blankStrings.add(null);

        return blankStrings;
    }

    private static List<String> invalidEmails(){

        return List.of("   Just a string",
                "string",
                "(comment)",
                "()@example.com",
                "fred(&)barny@example.com",
                "fred\\ barny@example.com",
                "Abigail <abi gail @ example.com>",
                "Abigail <abigail(fo(o)@example.com>",
                "Abigail <abigail(fo) o)@example.com>",
                "\"Abi\"gail\" <abigail@example.com>",
                "abigail@[exa]ple.com]",
                "abigail@[exa[ple.com]",
                "abigail@[exaple].com]",
                "abigail@",
                "@example.com",
                "phrase: abigail@example.com abigail@example.com ;",
                "invalid�char@example.com"
        );
    }

}
