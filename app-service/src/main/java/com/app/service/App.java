package com.app.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages=("com.app"))
@EnableEurekaClient
@MapperScan("com.app.dao")
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run( App.class, args );
    }
}
