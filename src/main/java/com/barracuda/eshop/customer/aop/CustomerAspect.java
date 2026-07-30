package com.barracuda.eshop.customer.aop;

import com.barracuda.eshop.customer.exception.ExceptionTranslator;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DuplicateKeyException;

@Aspect
public class CustomerAspect {

    private final ExceptionTranslator exceptionTranslator;

    public CustomerAspect(ExceptionTranslator exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    @Pointcut("execution(* com.barracuda.eshop.customer.service.*.*(..))")
    private void services(){

    }

    @AfterThrowing(pointcut = "services()",throwing = "ex")
    public void translateException(DuplicateKeyException ex){
        exceptionTranslator.translate(ex);
    }


}
