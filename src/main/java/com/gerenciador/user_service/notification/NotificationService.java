package com.gerenciador.user_service.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private final RestTemplate restTemplate;

    @Autowired
    public NotificationService(@Qualifier("notificationRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
