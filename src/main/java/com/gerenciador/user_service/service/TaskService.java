package com.gerenciador.user_service.service;

import com.gerenciador.user_service.model.Task;
import com.gerenciador.user_service.respository.TaskRepository;
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

    //lista de tarefas
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    //busca por id
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found")); //se nao existir mostra exceção
    }