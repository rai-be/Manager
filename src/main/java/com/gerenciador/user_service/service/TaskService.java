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

    @Transactional // Garante que a operação será executada em uma transação
    public Task createTask(Task task) {
        task.setCreatedAt(LocalDateTime.now()); // Define a data de criação como o momento atual
        task.setUpdatedAt(LocalDateTime.now()); // Define a data de atualização também como o momento atual
        return taskRepository.save(task); // Salva a tarefa no banco de dados
    }