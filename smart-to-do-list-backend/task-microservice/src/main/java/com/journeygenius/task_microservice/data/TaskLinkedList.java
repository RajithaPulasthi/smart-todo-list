package com.journeygenius.task_microservice.data;

public class TaskLinkedList {
    private TaskLinkedListNode head; // Head of the linked list

    // Constructor to initialize an empty linked list
    public TaskLinkedList() {
        this.head = null; // Initialize head as null
    }

    // Insert a new node at the beginning of the linked list
    public void insertNodeBegin(Task task) {
        TaskLinkedListNode newNode = new TaskLinkedListNode(task);
        newNode.setNext(head); // Link new node to the current head
        head = newNode; // Update head to new node
    }

    // Insert a new node at the end of the linked list
    public void insertNodeEnd(Task task) {
        TaskLinkedListNode newNode = new TaskLinkedListNode(task);
        if (head == null) {
            head = newNode; // If list is empty, set head to new node
        } else {
            TaskLinkedListNode current = head;
            while (current.getNext() != null) {
                current = current.getNext(); // Traverse to the end of the list
            }
            current.setNext(newNode); // Link new node at the end
        }
    }

    // Insert a new node after a specified node
    public void insertNodeAfter(TaskLinkedListNode prevNode, Task task) {
        if (prevNode == null) {
            System.out.println("The given previous node cannot be null.");
            return;
        }
        TaskLinkedListNode newNode = new TaskLinkedListNode(task);
        newNode.setNext(prevNode.getNext()); // Link new node to next of prev node
        prevNode.setNext(newNode); // Link prev node to new node
    }

    // Delete the first node in the linked list
    public void deleteNodeBegin() {
        if (head != null) {
            head = head.getNext(); // Update head to point to next node
        }
    }

    // Delete the last node in the linked list
    public void deleteNodeEnd() {
        if (head == null) return; // If list is empty, do nothing

        if (head.getNext() == null) {
            head = null; // If only one element, set head to null
            return;
        }

        TaskLinkedListNode secondLast = head;
        while (secondLast.getNext().getNext() != null) {
            secondLast = secondLast.getNext(); // Traverse to second last node
        }
        secondLast.setNext(null); // Remove reference to last node
    }

    // Delete a selected node based on task name (or any other criteria)
    public void deleteNodeSelected(String taskName) {
        if (head == null) return; // If list is empty, do nothing

        if (head.getTask().getName().equals(taskName)) {
            head = head.getNext(); // If head needs to be removed
            return;
        }

        TaskLinkedListNode current = head;
        while (current.getNext() != null && !current.getNext().getTask().getName().equals(taskName)) {
            current = current.getNext(); // Traverse until we find the task or reach end
        }

        if (current.getNext() != null) {
            current.setNext(current.getNext().getNext()); // Remove reference to selected node
        }
    }

    // Get a node at a specific index
    public TaskLinkedListNode getNodeAtIndex(int index) {
        TaskLinkedListNode current = head;
        int count = 0;

        while (current != null) {
            if (count == index) return current; // Return the node at the specified index
            count++;
            current = current.getNext();
        }

        return null; // If index is out of bounds, return null
    }

    // Getters and setters for head and other properties can be added here if needed.

    public TaskLinkedListNode getHead() {
        return head;
    }

    public int size() {
        return 0;
    }

    public void setHead(TaskLinkedListNode head) {
        this.head = head;
    }
}
