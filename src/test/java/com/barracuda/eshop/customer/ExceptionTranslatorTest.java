package com.barracuda.eshop.customer;

import com.barracuda.eshop.customer.exception.DuplicateEmailCustomerException;
import com.barracuda.eshop.customer.exception.ExceptionTranslator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExceptionTranslatorTest {

    private final ExceptionTranslator exceptionTranslator = new ExceptionTranslator();

    @Test
    void shouldTranslateDuplicateKeyExceptionToDuplicateCustomerEmailExceptionIfTheExceptionMessageContainsEmailConstraintViolation() {

        assertThatThrownBy(() -> exceptionTranslator.translate(new DuplicateKeyException("email_unique_constraint"))).isInstanceOf(DuplicateEmailCustomerException.class);

    }

    @Test
    void shouldNotTranslateRethrowTheProvidedExceptionIfTranslationIsNotPossible() {
        var exception = new DuplicateKeyException("UNKNOWN_ERROR");
        assertThatThrownBy(() -> exceptionTranslator.translate(exception)).isEqualTo(exception);
    }
}
