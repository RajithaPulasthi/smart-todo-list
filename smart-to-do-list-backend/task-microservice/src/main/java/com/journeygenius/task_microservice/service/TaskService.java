package com.journeygenius.task_microservice.service;

import com.journeygenius.task_microservice.data.Task;
import com.journeygenius.task_microservice.data.TaskLinkedList;
import com.journeygenius.task_microservice.data.TaskLinkedListNode;
import com.journeygenius.task_microservice.data.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    private TaskLinkedList taskLinkedList = new TaskLinkedList(); // Main linked list to manage tasks

    // Add a new task to the database and linked list
    public Task addTask(String name, Integer priority, Integer location_id) {
        Task task = new Task(name, priority, location_id);
        Task savedTask = taskRepository.save(task); // Save to database
        taskLinkedList.insertNodeEnd(savedTask); // Add to linked list
        return savedTask;
    }

    // Reorder the task linked list based on priority
    public TaskLinkedList reorderByPriority() {
        TaskLinkedList sortedList = new TaskLinkedList();
        TaskLinkedListNode current = taskLinkedList.getHead();

        while (current != null) {
            // Create a new node for the current task
            TaskLinkedListNode newNode = new TaskLinkedListNode(current.getTask());

            // Insert the new node into the sorted linked list
            insertInSortedOrder(sortedList, newNode);

            current = current.getNext(); // Move to the next node in the original list
        }

        return sortedList; // Return the newly sorted linked list
    }

    // Helper method to insert a node into the sorted linked list
    private void insertInSortedOrder(TaskLinkedList sortedList, TaskLinkedListNode newNode) {
        if (sortedList.getHead() == null ||
                newNode.getTask().getPriority() < sortedList.getHead().getTask().getPriority()) {
            // Insert at the beginning if the sorted list is empty or if new node has higher priority
            newNode.setNext(sortedList.getHead());
            sortedList.setHead(newNode); // Update head to the new node
        } else {
            // Find the correct position to insert
            TaskLinkedListNode current = sortedList.getHead();
            while (current.getNext() != null &&
                    current.getNext().getTask().getPriority() <= newNode.getTask().getPriority()) {
                current = current.getNext(); // Traverse until we find the position
            }

            // Insert the new node in its correct position
            newNode.setNext(current.getNext());
            current.setNext(newNode);
        }
    }


    // Reorder the task linked list based on distance (mock implementation)
    public TaskLinkedList reorderByDistance() {
        // This implementation assumes that we can get distances from a Location Microservice.
        // For demonstration purposes, we'll just reorder by priority as a placeholder.

        return reorderByPriority(); // Placeholder: replace with actual distance-based logic
    }

    // Show the task linked list based on criteria (default, priority, distance)
    public ResponseEntity<List<Task>> showTasks(String criteria) {
        TaskLinkedList displayList;

        switch (criteria.toLowerCase()) {
            case "priority":
                displayList = reorderByPriority();
                break;
            case "distance":
                displayList = reorderByDistance();
                break;
            case "default":
            default:
                displayList = taskLinkedList; // Default is the original linked list
                break;
        }

        // Collect tasks into a List for JSON response
        List<Task> taskList = new ArrayList<>();
        TaskLinkedListNode current = displayList.getHead();

        while (current != null) {
            taskList.add(current.getTask()); // Add each task to the list
            current = current.getNext();
        }

        return ResponseEntity.ok(taskList); // Return the list as a JSON response
    }

    // Additional methods for other functionalities can be added here.
}
