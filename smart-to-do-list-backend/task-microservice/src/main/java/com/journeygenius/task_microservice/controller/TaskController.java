package com.journeygenius.task_microservice.controller;

import com.journeygenius.task_microservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

}
