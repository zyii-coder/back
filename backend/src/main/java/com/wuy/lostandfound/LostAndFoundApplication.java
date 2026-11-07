package com.wuy.lostandfound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point.
 * @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
 * Component scan starts at this package, so every layer (controller/service/repository)
 * living under com.wuy.lostandfound is discovered automatically.
 */
@SpringBootApplication
public class LostAndFoundApplication {

    public static void main(String[] args) {
        SpringApplication.run(LostAndFoundApplication.class, args);
    }
}
