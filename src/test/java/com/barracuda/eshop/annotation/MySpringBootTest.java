package com.barracuda.eshop.annotation;


import com.barracuda.eshop.TestcontainersConfiguration;
import com.barracuda.eshop.config.InfrastructureConfiguration;
import com.barracuda.eshop.config.SpringDevSecurityConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@ActiveProfiles("test")
@Transactional
@Import({SpringDevSecurityConfiguration.class, InfrastructureConfiguration.class, TestcontainersConfiguration.class})
@SpringBootTest
@Retention(RetentionPolicy.RUNTIME)
public @interface MySpringBootTest {
}
