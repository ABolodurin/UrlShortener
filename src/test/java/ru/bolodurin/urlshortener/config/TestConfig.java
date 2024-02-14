package ru.bolodurin.urlshortener.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestConfig {
    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> dbContainer(DynamicPropertyRegistry registry) {
        var container = new PostgreSQLContainer<>("postgres:alpine");
        registry.add("postgresql.driver", container::getDriverClassName);
        return container;
    }

}
