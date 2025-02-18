package com.gerenciador.user_service.controller;

import com.gerenciador.user_service.dto.UserDTO;
import com.gerenciador.user_service.model.User;
import com.gerenciador.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO) {
        // Chama o método de validação para verificar se os dados do dto estão corretos.
        List<String> validationErrors = validateUserDTO(userDTO);

        if (!validationErrors.isEmpty()) {
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }

        // Converte o DTO para uma entidade User.
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Método privado para validar a dto antes de tentar criar um usuario.
    private List<String> validateUserDTO(UserDTO userDTO) {
        // Lista para armazenar possiveis erros de validação.
        List<String> errors = new ArrayList<>();

        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            errors.add("O nome de usuario é obrigatório");
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            errors.add("O email é obrigatório");
        } else {
            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            if (!userDTO.getEmail().matches(emailRegex)) {
                errors.add("O email deve ser valido");
            }
        }

        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            errors.add("A senha é obrigatório");
        }
        return errors;
    }
}
//mantermos comentarios enquanto estivermos realizando para sabermos as mudanças
