package com.gerenciador.user_service.taskService.repository;

import com.gerenciador.user_service.taskService.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}