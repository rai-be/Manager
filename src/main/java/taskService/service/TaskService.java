package taskService.service;

import taskService.enums.TaskHistory;
import taskService.model.Task;
import taskService.notificationClient.NotificationClient;
import taskService.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final NotificationClient notificationClient;

    // Construtor manual para injeção de dependências
    public TaskService(TaskRepository taskRepository, NotificationClient notificationClient) {
        this.taskRepository = taskRepository;
        this.notificationClient = notificationClient;
    }

    // Lista de tarefas
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Busca por ID
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Transactional
    public Task createTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Long id, Task taskDetails) {
        Task task = getTaskById(id);

        // Criar histórico antes de modificar os valores
        saveTaskHistory(task, "title", task.getTitle(), taskDetails.getTitle());
        saveTaskHistory(task, "description", task.getDescription(), taskDetails.getDescription());

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCategory(taskDetails.getCategory());
        task.setPriority(taskDetails.getPriority());
        task.setCompleted(taskDetails.isCompleted());
        task.setUpdatedAt(LocalDateTime.now());

        // Se a tarefa foi concluída, enviar notificação
        if (!task.isCompleted() && taskDetails.isCompleted()) {
            notificationClient.sendNotification("Task completed: " + task.getTitle());
        }

        return taskRepository.save(task);
    }

    // Deletar tarefa do banco
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    // Salvar histórico de alterações
    private void saveTaskHistory(Task task, String field, String oldValue, String newValue) {
        if (oldValue != null && !oldValue.equals(newValue)) {
            TaskHistory history = new TaskHistory();
            history.setTaskId(task.getId());
            history.setFieldName(field);
            history.setOldValue(oldValue);
            history.setNewValue(newValue);
            history.setModifiedAt(LocalDateTime.now());
            taskHistoryRepository.save(history);
        }
    }
}
