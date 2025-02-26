package taskService.notificationClient;

import taskService.enums.TaskPriority;
import taskService.model.Task;
import taskService.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledTaskNotifier {

    private final TaskRepository taskRepository;
    private final NotificationClient notificationClient;


    // Verificar tarefas pendentes a cada 6 horas
    @Scheduled(fixedRate = 21600000) // 6 horas em milissegundos
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