package com.app.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages=("com.app"))
@EnableEurekaClient
@EnableTransactionManagement
@MapperScan("com.app.dao")
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run( App.class, args );
    }
}
