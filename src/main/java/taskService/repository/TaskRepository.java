package taskService.repository;

import org.springframework.stereotype.Repository;
import taskService.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompletedFalse();
}