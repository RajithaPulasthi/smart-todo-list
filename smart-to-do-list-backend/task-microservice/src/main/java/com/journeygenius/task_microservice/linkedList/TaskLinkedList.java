package com.journeygenius.task_microservice.linkedList;

public class TaskLinkedList {
    TaskNode head;

    public TaskLinkedList() {
        head = null;
    }

    public void insertNodeBegin(int id, String name, int priority, int locationId) {
        TaskNode new_node = new TaskNode(id, name, priority, locationId);

        if (head == null) {
            head = new_node;
        } else {
            head.previous = new_node;
            new_node.next = head;
            head = new_node; // Update head to new node
        }
    }

    public void insertNodeEnd(int id, String name, int priority, int locationId) {
        TaskNode new_node = new TaskNode(id, name, priority, locationId);

        if (head == null) {
            head = new_node;
        } else {
            TaskNode node = head;
            while (node.next != null) {
                node = node.next;
            }
            node.next = new_node;
            new_node.previous = node;
        }
    }

    public void insertNodeAfter(int index, int id, String name, int priority, int locationId) throws IllegalArgumentException {
        TaskNode node = getNodeAtIndex(index);

        if (node == null) {
            throw new IllegalArgumentException("No node exists at the specified index: " + index);
        }

        TaskNode new_node = new TaskNode(id, name, priority, locationId);
        new_node.next = node.next;
        new_node.previous = node;

        if (node.next != null) {
            node.next.previous = new_node;
        }

        node.next = new_node;
    }

    public void deleteNodeBegin() throws IllegalStateException {
        if (head == null) {
            throw new IllegalStateException("Cannot delete from an empty list.");
        }

        head = head.next;

        if (head != null) {
            head.previous = null; // Update previous of the new head
        }
    }

    public void deleteNodeEnd() throws IllegalStateException {
        if (head == null) {
            throw new IllegalStateException("Cannot delete from an empty list.");
        }

        if (head.next == null) { // Only one element in the list
            head = null;
            return;
        }

        TaskNode node = head;
        while (node.next != null) {
            node = node.next;
        }

        node.previous.next = null; // Remove last node
    }

    public void deleteNodeSelected(int index) throws IllegalArgumentException {
        TaskNode node = getNodeAtIndex(index);

        if (node == null || head == null) {
            throw new IllegalArgumentException("No node exists at the specified index: " + index);
        }

        if (node == head) { // If the node to delete is the head
            deleteNodeBegin();
            return;
        }

        if (node.next != null) { // If it's not the last node
            node.next.previous = node.previous;
        }

        if (node.previous != null) { // If it's not the first node
            node.previous.next = node.next;
        }
    }

    private TaskNode getNodeAtIndex(int index) {
        if (index < 0) return null; // Invalid index

        TaskNode current = head;
        for (int i = 0; i < index && current != null; i++) {
            current = current.next;
        }

        return current; // Returns the found node or null if not found
    }
}
