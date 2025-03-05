package taskService.enums;

import java.time.LocalDateTime;

public class TaskCommentService {
    private final TaskCommentRepository taskCommentRepository;

    public TaskComment addComment(Long taskId, String comment, String author) {
        TaskComment taskComment = new TaskComment();
        taskComment.setTaskId(taskId);
        taskComment.setComment(comment);
        taskComment.setAuthor(author);
        taskComment.setCreatedAt(LocalDateTime.now());

        return taskCommentRepository.save(taskComment);
    }
}
