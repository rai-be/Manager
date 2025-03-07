package com.gerenciador.user_service.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import com.gerenciador.user_service.model.TaskHistory;
import com.gerenciador.user_service.model.Task;
import com.gerenciador.user_service.respository.TaskHistoryRepository;
import com.gerenciador.user_service.notificationClient.NotificationClient;
import com.gerenciador.user_service.respository.TaskRepository;
import com.gerenciador.user_service.respository.TaskCommentRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final NotificationClient notificationClient;
    private final TaskHistoryRepository taskHistoryRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public TaskService(
            TaskRepository taskRepository,
            NotificationClient notificationClient,
            TaskHistoryRepository taskHistoryRepository,
            TaskCommentRepository taskCommentRepository,
            RestTemplate restTemplate) {
        this.taskRepository = taskRepository;
        this.notificationClient = notificationClient;
        this.taskHistoryRepository = taskHistoryRepository;
        this.taskCommentRepository = taskCommentRepository;
        this.restTemplate = restTemplate;
    }

    // Chamada ao UserService para buscar informações sobre o usuário
    public String getUserInfoFromUserService(Long userId) {
        String userServiceUrl = "http://localhost:8085/user-service/user/" + userId;
        return restTemplate.getForObject(userServiceUrl, String.class);  // Chama o UserService
    }

    // Lista de tarefas
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Busca por ID
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    @Transactional
    public Task createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Long id, Task taskDetails) {
        if (taskDetails == null) {
            throw new IllegalArgumentException("Task details cannot be null");
        }

        Task task = getTaskById(id);

        saveTaskHistory(task, "title", task.getTitle(), taskDetails.getTitle());
        saveTaskHistory(task, "description", task.getDescription(), taskDetails.getDescription());

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCategory(taskDetails.getCategory());
        task.setPriority(taskDetails.getPriority());
        task.setCompleted(taskDetails.isCompleted());
        task.setUpdatedAt(LocalDateTime.now());

        if (!task.isCompleted() && taskDetails.isCompleted()) {
            notificationClient.sendNotification("Task completed: " + task.getTitle());
        }

        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    private void saveTaskHistory(Task task, String field, String oldValue, String newValue) {
        if (oldValue != null && !Objects.equals(oldValue, newValue)) {
            TaskHistory history = new TaskHistory();
            history.setTaskId(task.getId());
            history.setFieldName(field);
            history.setOldValue(oldValue);
            history.setNewValue(newValue);
            history.setModifiedAt(LocalDateTime.now());

            taskHistoryRepository.save(history);
        }
    }

    public static class TaskNotFoundException extends RuntimeException {
        public TaskNotFoundException(String message) {
            super(message);
        }

        public TaskNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
