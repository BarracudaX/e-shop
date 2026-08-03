package com.barracuda.eshop.customer.exception

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.dao.DuplicateKeyException

class ExceptionTranslatorTest {
    private val exceptionTranslator = ExceptionTranslator()

    @Test
    fun `should translate DuplicateKeyException to DuplicateCustomerEmailException if the exception message contains unique email constraint violation`() {
        assertThatThrownBy {
            exceptionTranslator.translate(DuplicateKeyException("email_unique_constraint"))
        }.isInstanceOf(DuplicateEmailCustomerException::class.java)
    }

    @Test
    fun `should not translate and rethrow the provided exception if the exception is not recognized`() {
        val exception = DuplicateKeyException("UNKNOWN_ERROR")
        assertThatThrownBy { exceptionTranslator.translate(exception) }.isEqualTo(exception)
    }
}
