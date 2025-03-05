package taskService.service;

import taskService.model.TaskComment;
import taskService.repository.TaskCommentRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskCommentService {

    private final TaskCommentRepository taskCommentRepository;

    public TaskCommentService(TaskCommentRepository taskCommentRepository) {
        this.taskCommentRepository = taskCommentRepository;
    }

    @Transactional
    public TaskComment addComment(Long taskId, String comment, String author) {
        TaskComment taskComment = new TaskComment();
        taskComment.setTaskId(taskId);
        taskComment.setComment(comment);
        taskComment.setAuthor(author);
        taskComment.setCreatedAt(LocalDateTime.now());

        return taskCommentRepository.save(taskComment);
    }

    public List<TaskComment> getCommentsByTaskId(Long taskId) {
        return taskCommentRepository.findByTaskId(taskId);
    }
}
