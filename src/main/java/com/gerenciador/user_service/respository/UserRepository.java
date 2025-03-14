package com.gerenciador.user_service.respository;

import com.gerenciador.user_service.model.Task;
import com.gerenciador.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    List<Task> findTasksById(String userId);

}


