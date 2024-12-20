package com.journeygenius.task_microservice.data;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "priority")
    private int priority;

    @Column(name = "location_id")
    private int location_id;

    public int getId() {
        return id;
    }

    public Task(){
    }

    public Task(String name, int priority, int location_id) {
        this.name = name;
        this.priority = priority;
        this.location_id = location_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }
}
