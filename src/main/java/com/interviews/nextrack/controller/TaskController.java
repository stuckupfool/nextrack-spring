package com.interviews.nextrack.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.interviews.nextrack.service.TaskService;
import com.interviews.nextrack.todo.Task;

@RestController
@RequestMapping("/api/todos")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTodos() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTodoById(@PathVariable("id") Long id) {
        Task task = taskService.getTask(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTodo(@RequestBody Task task) {
        Task createdTask = taskService.saveTask(task);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTodo(@PathVariable("id") Long id, @RequestBody Task taskDetails) {
        Task existingTask = taskService.getTask(id);
        if (existingTask == null) {
            return ResponseEntity.notFound().build();
        }
        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setCompleted(taskDetails.getCompleted());
        existingTask.setRank(taskDetails.getRank());
        Task updatedTask = taskService.saveTask(existingTask);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleTodoCompletion(@PathVariable("id") Long id) {
        Task task = taskService.getTask(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        taskService.toggleTaskCompletion(id);
        Task updatedTask = taskService.getTask(id);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable("id") Long id) {
        Task existingTask = taskService.getTask(id);
        if (existingTask == null) {
            return ResponseEntity.notFound().build();
        }
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
