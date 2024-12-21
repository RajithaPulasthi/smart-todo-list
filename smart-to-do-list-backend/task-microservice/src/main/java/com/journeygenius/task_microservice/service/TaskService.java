package com.journeygenius.task_microservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.journeygenius.task_microservice.data.Task;
import com.journeygenius.task_microservice.data.TaskLinkedList;
import com.journeygenius.task_microservice.data.TaskLinkedListNode;
import com.journeygenius.task_microservice.data.TaskRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

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


    public TaskLinkedList reorderByDistance(Integer sourceId) {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Construct the URL for the API call
        String url = "http://localhost:8094/journey-genie-backend-api/locations/optimal-path?sourceId=" + sourceId;

        // Fetch the result as a String
        String result = restTemplate.getForObject(url, String.class);

        // Check if the result is a valid JSON array
        if (result.startsWith("[") && result.endsWith("]")) {
            // Parse the JSON array
            JSONArray jsonArray = new JSONArray(result);

            // Loop through each object in the array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int locationId = jsonObject.getInt("id");
                //System.out.println("Location ID: " + locationId);

                // Fetch the task for this location ID
                Task task = fetchTaskByLocationId(locationId);
                if (task != null) {
                    // Add the task to the linked list
                    taskLinkedList.insertNodeEnd(task);
                } else {
                    System.out.println("No task found for Location ID: " + locationId);
                }
            }
        } else {
            System.out.println("Received data is not a valid JSON array.");
        }

        return taskLinkedList; // Return appropriate TaskLinkedList as needed
    }

    private Task fetchTaskByLocationId(int locationId) {
        return taskRepository.findByLocationId(locationId);
    }


    // Show the task linked list based on criteria (default, priority, distance)
    public ResponseEntity<List<Task>> showTasks(String criteria) {
        TaskLinkedList displayList;

        switch (criteria.toLowerCase()) {
            case "priority":
                displayList = reorderByPriority();
                break;
            case "distance":
                displayList = reorderByDistance(1);
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
