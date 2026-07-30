package com.barracuda.eshop;

import org.springframework.boot.SpringApplication;

public class TestEShopApplication {

    public static void main(String[] args) {
        SpringApplication.from(EShopApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
