package taskService.service;

import org.springframework.beans.factory.annotation.Autowired;
import taskService.model.TaskHistory;
import taskService.model.Task;
import taskService.model.TaskComment;
import taskService.repository.TaskHistoryRepository;
import taskService.notificationClient.NotificationClient;
import taskService.repository.TaskRepository;
import taskService.repository.TaskCommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final NotificationClient notificationClient;
    private final TaskHistoryRepository taskHistoryRepository;
    private final TaskCommentRepository taskCommentRepository;

    // Construtor manual para injeção de dependências
    @Autowired
    public TaskService(TaskRepository taskRepository,
                       NotificationClient notificationClient,
                       TaskHistoryRepository taskHistoryRepository, TaskCommentRepository taskCommentRepository) {
        this.taskRepository = taskRepository;
        this.notificationClient = notificationClient;
        this.taskHistoryRepository = taskHistoryRepository;
        this.taskCommentRepository = taskCommentRepository;
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
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    // Salvar histórico de alterações
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

    public static class TaskCommentService {
        private final TaskCommentRepository taskCommentRepository;

        // Construtor para injeção de dependência
        @Autowired
        public TaskCommentService(TaskCommentRepository taskCommentRepository) {
            this.taskCommentRepository = taskCommentRepository;
        }

        public TaskComment addComment(Long taskId, String comment, String author) {
            if (comment == null || comment.isBlank()) {
                throw new IllegalArgumentException("Comment cannot be null or blank");
            }
            if (author == null || author.isBlank()) {
                throw new IllegalArgumentException("Author cannot be null or blank");
            }

            TaskComment taskComment = new TaskComment();
            taskComment.setTaskId(taskId);
            taskComment.setComment(comment);
            taskComment.setAuthor(author);
            taskComment.setCreatedAt(LocalDateTime.now());

            return taskCommentRepository.save(taskComment);
        }
    }

    // Exceção personalizada para tarefas não encontradas
    public static class TaskNotFoundException extends RuntimeException {
        public TaskNotFoundException(String message) {
            super(message);
        }
    }
}