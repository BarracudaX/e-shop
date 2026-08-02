package com.barracuda.eshop.customer

import com.barracuda.eshop.annotation.MyMvcTest
import com.barracuda.eshop.customer.dto.CustomerRegistrationForm
import com.barracuda.eshop.customer.exception.DuplicateEmailCustomerException
import com.barracuda.eshop.customer.service.CustomerService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.assertj.WebTestClientResponse
import org.springframework.test.web.reactive.server.assertj.WebTestClientResponseAssert
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.MockMvcWebTestClient

@MyMvcTest
class CustomerRegistrationTest : AbstractCustomerTest() {
    @MockitoBean
    private lateinit var customerService: CustomerService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setup() {
        webTestClient = MockMvcWebTestClient.bindTo(mockMvc).build()
    }

    @Test
    fun customerShouldBeAbleToRegister() {
        assertThat(sendRegistrationRequest(johnRegistrationFormBuilder.build())).hasStatus2xxSuccessful()
    }

    @Test
    fun shouldNotAllowRegisteringCustomerWithEmailThatIsAlreadyInUseByAnotherCustomer() {
        doNothing().doThrow(DuplicateEmailCustomerException::class.java)
            .`when`(customerService)!!
            .register(any(CustomerRegistrationForm::class.java))

        sendRegistrationRequest(johnRegistrationFormBuilder.build())

        assertThat(sendRegistrationRequest(johnRegistrationFormBuilder.build()))
            .hasStatus(HttpStatus.CONFLICT)
    }


    @MethodSource("blankStrings")
    @ParameterizedTest
    fun shouldNotAllowRegisteringCustomerWithEmptyFirstName(blankFirstName: String?) {
        val result = sendRegistrationRequest(johnRegistrationFormBuilder.withFirstName(blankFirstName).build())

        assertThat<WebTestClientResponseAssert?>(result)!!.hasStatus(HttpStatus.BAD_REQUEST)
    }

    @MethodSource("blankStrings")
    @ParameterizedTest
    fun shouldNotAllowRegisteringCustomerWithEmptyLastName(blankLastName: String?) {
        val result = sendRegistrationRequest(johnRegistrationFormBuilder.withLastName(blankLastName).build())

        assertThat<WebTestClientResponseAssert?>(result)!!.hasStatus(HttpStatus.BAD_REQUEST)
    }

    @MethodSource("invalidEmails")
    @ParameterizedTest
    fun shouldNotAllowRegisteringCustomerWithInvalidEmail() {
        val result = sendRegistrationRequest(johnRegistrationFormBuilder.withEmail("invalid_email").build())

        assertThat<WebTestClientResponseAssert?>(result)!!.hasStatus(HttpStatus.BAD_REQUEST)
    }

    @MethodSource("shortPasswords")
    @ParameterizedTest
    fun shouldNotAllowRegisteringCustomerWithPasswordLengthLessThan8Characters(shortPassword: String?) {
        val result = sendRegistrationRequest(johnRegistrationFormBuilder.withPassword(shortPassword).build())

        assertThat<WebTestClientResponseAssert?>(result)!!.hasStatus(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun shouldNotAllowRegisteringCustomerWithoutPassword() {
        val result = sendRegistrationRequest(johnRegistrationFormBuilder.withPassword(null).build())

        assertThat<WebTestClientResponseAssert?>(result)!!.hasStatus(HttpStatus.BAD_REQUEST)
    }

    @MethodSource("onlyDigitPasswords")
    @ParameterizedTest
    fun shouldNotAllowRegisterCustomerWithPasswordThatDoesNotContainLetters(onlyDigitPassword: String) {
        val result = sendRegistrationRequest(johnRegistrationFormBuilder.withPassword(onlyDigitPassword).build())

        assertThat<WebTestClientResponseAssert?>(result)!!.hasStatus(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun shouldNotAllowRegisteringCustomerWithPasswordThatDoesNotContainAtLeatOneSpecialCharacter() {
        val result = sendRegistrationRequest(johnRegistrationFormBuilder.withPassword("Password123").build())

        assertThat<WebTestClientResponseAssert?>(result)!!.hasStatus(HttpStatus.BAD_REQUEST)
    }

    @MethodSource("passwordSpecialCharacters")
    @ParameterizedTest
    fun successfulCustomerRegistrationWithPasswordContainingSpecialCharacter(specialCharacter: String) {
        val password = "Password123$specialCharacter"
        val result = sendRegistrationRequest(
            johnRegistrationFormBuilder.withPassword(password).withRepeatedPassword(password).build()
        )

        assertThat<WebTestClientResponseAssert?>(result)!!.hasStatus(HttpStatus.OK)
    }

    @Test
    fun shouldNotAllowRegisteringCustomerWhenTwoPasswordsDoNotMatch() {
        val result = sendRegistrationRequest(
            johnRegistrationFormBuilder.withPassword("Password123!").withRepeatedPassword("Password123").build()
        )

        assertThat<WebTestClientResponseAssert?>(result)!!.hasStatus(HttpStatus.BAD_REQUEST)
    }

    private fun sendRegistrationRequest(registrationForm: CustomerRegistrationForm): WebTestClientResponse {
        return WebTestClientResponse.from(
            webTestClient
                .post()
                .uri("/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationForm)
                .exchange()
        )
    }
}
