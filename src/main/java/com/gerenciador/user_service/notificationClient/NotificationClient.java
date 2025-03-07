package com.gerenciador.user_service.notificationClient;

import org.springframework.stereotype.Component;

@Component
public class NotificationClient {

    public void sendNotification(String message) {
        System.out.println("Notificação enviada: " + message);
    }
}
