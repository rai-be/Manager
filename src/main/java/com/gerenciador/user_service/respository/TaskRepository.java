package com.gerenciador.user_service.respository;

import com.gerenciador.user_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}