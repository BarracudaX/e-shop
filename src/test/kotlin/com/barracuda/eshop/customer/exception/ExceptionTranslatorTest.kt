package com.barracuda.eshop.customer.exception

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.dao.DuplicateKeyException

class ExceptionTranslatorTest {
    private val exceptionTranslator = ExceptionTranslator()

    @Test
    fun shouldTranslateDuplicateKeyExceptionToDuplicateCustomerEmailExceptionIfTheExceptionMessageContainsEmailConstraintViolation() {
        assertThatThrownBy {
            exceptionTranslator.translate(DuplicateKeyException("email_unique_constraint"))
        }.isInstanceOf(DuplicateEmailCustomerException::class.java)
    }

    @Test
    fun shouldNotTranslateRethrowTheProvidedExceptionIfTranslationIsNotPossible() {
        val exception = DuplicateKeyException("UNKNOWN_ERROR")
        assertThatThrownBy { exceptionTranslator.translate(exception) }.isEqualTo(exception)
    }
}
