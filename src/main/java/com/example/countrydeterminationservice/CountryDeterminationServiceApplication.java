package com.example.countrydeterminationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CountryDeterminationServiceApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CountryDeterminationServiceApplication.class, args);
    }

}
