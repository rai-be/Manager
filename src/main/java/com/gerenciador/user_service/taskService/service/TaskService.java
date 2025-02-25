package com.gerenciador.user_service.taskService.service;

import com.gerenciador.user_service.taskService.model.Task;
import com.gerenciador.user_service.notificationClient.NotificationClient;
import com.gerenciador.user_service.taskService.repository.TaskRepository;
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

    @Transactional
    public Task updateTask(Long id, Task taskDetails) {
        Task task = getTaskById(id); // Busca a tarefa pelo ID, se não existir, lança exceção.

        // Atualiza os campos da tarefa com os novos valores recebidos.
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCategory(taskDetails.getCategory());
        task.setPriority(taskDetails.getPriority());
        task.setCompleted(taskDetails.isCompleted());
        task.setUpdatedAt(LocalDateTime.now()); // Atualiza a data da última modificação.

        // Se a tarefa foi alterada de "não concluída" para "concluída", envia uma notificação.
        if (!task.isCompleted() && taskDetails.isCompleted()) {
            notificationClient.sendNotification("Task completed: " + task.getTitle());
        }

        return taskRepository.save(task); // Salva e retorna a tarefa atualizada.
    }

    //deletar tarefa no bd
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found"); // Lança erro se a tarefa não existir.
        }
        taskRepository.deleteById(id); // Remove a tarefa do banco de dados.
    }
}