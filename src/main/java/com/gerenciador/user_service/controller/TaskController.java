package com.gerenciador.user_service.controller;


import com.gerenciador.user_service.model.Task;
import com.gerenciador.user_service.service.TaskService;
//import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
//@RequiredArgsConstructor
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
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);    }

    //atuaizar e retornar tarefa
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    //remover a tarefa do bd
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}