package com.rishabh.mbta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Using a Spring Boot Application instead of a simple java console based project
 * so that the code can be extended for purposes of a web application later. Also, spring gives us handy
 * utilities like RestTemplate for consuming APIs and Jackson for parsing json responses
 */
@SpringBootApplication
public class MbtaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MbtaApplication.class, args);
    }


}