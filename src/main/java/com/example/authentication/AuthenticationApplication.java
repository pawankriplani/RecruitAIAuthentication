package com.example.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan("com.example.authentication.model")
@EnableJpaRepositories("com.example.authentication.repository")
@EnableTransactionManagement
public class AuthenticationApplication {

    @Value("${server.port}")
    private int serverPort;

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void printH2ConsoleUrl() {
        System.out.println("H2 Console: http://localhost:" + serverPort + "/h2-console");
    }
}
