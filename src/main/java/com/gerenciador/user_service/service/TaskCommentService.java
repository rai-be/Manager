package com.gerenciador.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gerenciador.user_service.model.TaskComment;
import com.gerenciador.user_service.respository.TaskCommentRepository;

import java.time.LocalDateTime;

@Service
public class TaskCommentService {

    private final TaskCommentRepository taskCommentRepository;

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
