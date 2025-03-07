package com.gerenciador.user_service.controller;

import com.gerenciador.user_service.dto.UserDTO;
import com.gerenciador.user_service.model.User;
import com.gerenciador.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUserByUsername(@RequestParam(required = false) String username) {
        if (username != null && !username.trim().isEmpty()) {
            User user = userService.getUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuário com o username " + username + " não encontrado"));
            return ResponseEntity.ok(List.of(user));
        }
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO) {
        List<String> validationErrors = validateUserDTO(userDTO);

        if (!validationErrors.isEmpty()) {
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        if (userService.existsByUsername(userDTO.getUsername())) {
            return new ResponseEntity<>("Usuário com o username " + userDTO.getUsername() + " já existe!", HttpStatus.BAD_REQUEST);
        }

        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    private List<String> validateUserDTO(UserDTO userDTO) {
        List<String> errors = new ArrayList<>();

        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            errors.add("O nome de usuário é obrigatório");
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            errors.add("O email é obrigatório");
        } else {
            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            if (!userDTO.getEmail().matches(emailRegex)) {
                errors.add("O email deve ser válido");
            }
        }

        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            errors.add("A senha é obrigatória");
        }
        return errors;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        boolean isDeleted = userService.deleteUserById(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        List<String> validationErrors = validateUserDTO(userDTO);

        if (!validationErrors.isEmpty()) {
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }

        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }

        if (userService.existsByUsername(userDTO.getUsername()) && !userDTO.getUsername().equals(existingUser.getUsername())) {
            return new ResponseEntity<>("Usuário com o username " + userDTO.getUsername() + " já existe!", HttpStatus.BAD_REQUEST);
        }

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPassword(userDTO.getPassword());
        User updatedUser = userService.saveUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }
}
