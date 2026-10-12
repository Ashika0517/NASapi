package com.ashika.nasapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NasaApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(NasaApiApplication.class, args);
    }
}
