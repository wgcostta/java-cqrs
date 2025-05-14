package com.example.cqrsdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.cqrsdemo.domain.repository")
public class CqrsConfig {
    // Configurações adicionais podem ser adicionadas aqui
}