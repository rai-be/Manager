package com.gerenciador.user_service.notificationClient;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.gerenciador.user_service.enums.TaskPriority;
import com.gerenciador.user_service.model.Task;
import com.gerenciador.user_service.respository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledTaskNotifier {

    private final TaskRepository taskRepository;
    private final NotificationClient notificationClient;

    public ScheduledTaskNotifier(TaskRepository taskRepository, NotificationClient notificationClient) {
        this.taskRepository = taskRepository;
        this.notificationClient = notificationClient;
    }

    @Scheduled(fixedRate = 21600000)
    public void checkPendingTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepository.findByCompletedFalse();

        for (Task task : tasks) {
            if (task.getPriority() == TaskPriority.CRITICAL) {
                notificationClient.sendNotification("⚠️ Tarefa urgente pendente: " + task.getTitle());
            }
        }
    }
}
