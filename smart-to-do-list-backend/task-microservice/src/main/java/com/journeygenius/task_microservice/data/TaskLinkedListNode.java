package com.journeygenius.task_microservice.data;

public class TaskLinkedListNode {
    private Task task; // The task object
    private TaskLinkedListNode next; // Pointer to the next node

    // Constructor to create a new node with a task
    public TaskLinkedListNode(Task task) {
        this.task = task;
        this.next = null; // Initialize next as null
    }

    // Getters and setters
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TaskLinkedListNode getNext() {
        return next;
    }

    public void setNext(TaskLinkedListNode next) {
        this.next = next;
    }
}
