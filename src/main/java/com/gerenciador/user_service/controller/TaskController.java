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

    //injeção de dependencia do service
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //retorna lista de tarefas concluidas
    @GetMapping
    public ResponseEntity<List<com.gerenciador.user_service.model.Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    //retorna tarefa por id
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    //cria e salva tarefa
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }
