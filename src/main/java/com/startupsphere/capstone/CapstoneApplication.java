package com.startupsphere.capstone;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CapstoneApplication {

    public static void main(String[] args) {
        //Load environment variables from .env
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SENDGRID_API_KEY", dotenv.get("SENDGRID_API_KEY"));

        SpringApplication.run(CapstoneApplication.class, args);
        System.out.println("Running");
    }

}
