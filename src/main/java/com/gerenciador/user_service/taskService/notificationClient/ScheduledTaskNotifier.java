package com.gerenciador.user_service.taskService.notificationClient;

import com.gerenciador.user_service.taskService.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledTaskNotifier {

    private final TaskRepository taskRepository;
    private final NotificationClient notificationClient;

}