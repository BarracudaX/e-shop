package com.barracuda.eshop.customer.exception;

import org.springframework.dao.DuplicateKeyException;

public class ExceptionTranslator {

    public void translate(DuplicateKeyException exception) {
        if(exception.getMessage().contains("email_unique_constraint")) {
            throw  new DuplicateEmailCustomerException();
        }

        throw exception;
    }

}
