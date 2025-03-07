package com.gerenciador.user_service.respository;

import com.gerenciador.user_service.model.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
}
