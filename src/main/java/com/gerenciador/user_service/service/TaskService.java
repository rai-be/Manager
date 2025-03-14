package com.gerenciador.user_service.service;

import org.apache.coyote.BadRequestException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final NotificationClient notificationClient;
    private final TaskHistoryRepository taskHistoryRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final RestTemplate restTemplate;

    private final Map<Long, String> userInfoCache = new HashMap<>();

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

    public String getUserInfoFromUserService(Long userId) {
        if (userInfoCache.containsKey(userId)) {
            return userInfoCache.get(userId);
        }

        String userInfo = restTemplate.getForObject("http://localhost:8080/user-service/user/" + userId, String.class);
        userInfoCache.put(userId, userInfo);

        return userInfo;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    @Transactional
    public Task createTask(Task task) throws BadRequestException {
        if (task == null) {
            throw new BadRequestException("Task cannot be null");
        }
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Long id, Task taskDetails) throws BadRequestException {
        if (taskDetails == null) {
            throw new BadRequestException("Task details cannot be null");
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

    public List<Task> getTasksByUser(String userId) {
        try {
            String userInfo = getUserInfoFromUserService(Long.valueOf(userId));
            if (userInfo == null || userInfo.isEmpty()) {
                throw new TaskNotFoundException("User not found with id: " + userId);
            }
            return taskRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new TaskNotFoundException("Error while fetching user info or tasks for userId: " + userId, e);
        }
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
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
