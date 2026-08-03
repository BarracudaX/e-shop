package com.barracuda.eshop.annotation;

import com.barracuda.eshop.config.SpringDevSecurityConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ActiveProfiles("test")
@Import({SpringDevSecurityConfiguration.class})
@WebMvcTest
@Retention(RetentionPolicy.RUNTIME)
public @interface MyMvcTest {

}
