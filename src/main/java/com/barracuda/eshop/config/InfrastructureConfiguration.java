package com.barracuda.eshop.config;

import com.barracuda.eshop.customer.validation.EqualPasswordsValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureConfiguration {

    @Bean
    public EqualPasswordsValidator  equalPasswordsValidator() {
        return new EqualPasswordsValidator();
    }

}
