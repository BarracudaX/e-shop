package com.barracuda.eshop.annotation;


import com.barracuda.eshop.TestcontainersConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@ActiveProfiles("test")
@Transactional
@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Retention(RetentionPolicy.RUNTIME)
public @interface MySpringBootTest {
}
