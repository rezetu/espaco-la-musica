package com.example.sistemaescolar;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.sistemaescolar.repository")
@EntityScan(basePackages = "com.example.sistemaescolar.model")
public class JpaConfig {
    // Não é necessário adicionar nenhum método aqui
}


