package com.gerenciador.user_service.notificationClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-service", url = "http://localhost:8082")
public interface NotificationClient {

    @PostMapping("/notifications/send")
    void sendNotification(@RequestParam String message);
}