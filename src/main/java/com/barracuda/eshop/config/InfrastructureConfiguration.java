package com.barracuda.eshop.config;

import com.barracuda.eshop.customer.aop.CustomerAspect;
import com.barracuda.eshop.customer.exception.ExceptionTranslator;
import com.barracuda.eshop.customer.validation.EqualPasswordsValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureConfiguration {

    @Bean
    public EqualPasswordsValidator  equalPasswordsValidator() {
        return new EqualPasswordsValidator();
    }

    @Bean
    public CustomerAspect customerAspect(ExceptionTranslator exceptionTranslator) {
        return new CustomerAspect(exceptionTranslator);
    }

    @Bean
    public ExceptionTranslator exceptionTranslator() {
        return new ExceptionTranslator();
    }
}
