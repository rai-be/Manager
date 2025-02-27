package com.gerenciador.user_service.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.gerenciador.user_service.notificationClient")
public class AppConfig {
}
