package com.journeygenius.task_microservice.linkedList;

public class TaskNode {
    private int id;
    private String name;
    private int priority;
    private int locationId;

    protected TaskNode previous;
    protected TaskNode next;

    public TaskNode(int id, String name, int priority, int locationId) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.locationId = locationId;

        this.previous = null;
        this.next = null;
    }
}
