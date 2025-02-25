package com.gerenciador.user_service.controller;


import com.gerenciador.user_service.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {


    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<com.gerenciador.user_service.model.Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }


