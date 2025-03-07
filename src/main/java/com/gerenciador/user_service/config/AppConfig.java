package com.gerenciador.user_service.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @Qualifier("notificationRestTemplate")
    public RestTemplate notificationRestTemplate() {
        return new RestTemplate();
    }
}
