package com.journeygenius.task_microservice.controller;

import com.journeygenius.task_microservice.data.Task;
import com.journeygenius.task_microservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Endpoint to add a new task
    @PostMapping("/")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.addTask(task.getName(), task.getPriority(), task.getLocation_id());
        return ResponseEntity.ok(createdTask);
    }

    // Endpoint to reorder tasks by priority
    @GetMapping("/reorder/priority")
    public ResponseEntity<String> reorderTasksByPriority() {
        taskService.reorderByPriority(); // Reorder tasks based on priority
        return ResponseEntity.ok("Tasks reordered by priority.");
    }

    // Endpoint to reorder tasks by distance (placeholder)
    @GetMapping("/reorder/distance")
    public ResponseEntity<String> reorderTasksByDistance(@RequestParam Integer sourceId) {
        taskService.reorderByDistance(sourceId); // Reorder tasks based on distance (mock implementation)
        return ResponseEntity.ok("Tasks reordered by distance (mock implementation).");
    }

    // Endpoint to show tasks based on criteria
    @GetMapping("/show")
    public ResponseEntity<List<Task>> showTasks(@RequestParam String criteria) {
        return taskService.showTasks(criteria); // Call service method and return its response
    }

}
