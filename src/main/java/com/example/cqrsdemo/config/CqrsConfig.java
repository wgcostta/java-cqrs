package com.example.cqrsdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.cqrsdemo.domain.repository")
public class CqrsConfig {
    // Configurações adicionais podem ser adicionadas aqui
}