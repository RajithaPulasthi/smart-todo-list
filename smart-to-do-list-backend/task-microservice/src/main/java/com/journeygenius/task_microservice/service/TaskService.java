package com.journeygenius.task_microservice.service;

import com.journeygenius.task_microservice.data.TaskRepository;
import com.journeygenius.task_microservice.linkedList.TaskLinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskLinkedList taskLinkedList;


}
