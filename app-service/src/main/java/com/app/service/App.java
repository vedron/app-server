package com.app.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages=("com.app"))
@EnableEurekaClient
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run( App.class, args );
    }
}
