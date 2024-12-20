package com.journeygenius.location_microservice.data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationGraphNode {
    private Location location; // The location object
    private List<LocationGraphNode> neighbors; // List of neighboring nodes
    private Map<LocationGraphNode, Double> weights; // Map of weights to neighbors

    // Constructor to create a new graph node with a location
    public LocationGraphNode(Location location) {
        this.location = location;
        this.neighbors = new ArrayList<>(); // Initialize neighbors list
        this.weights = new HashMap<>(); // Initialize weights map
    }

    public Location getLocation() {
        return location;
    }

    public List<LocationGraphNode> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(LocationGraphNode neighbor, double weight) {
        this.neighbors.add(neighbor); // Add a neighbor to the list
        this.weights.put(neighbor, weight); // Store the weight for this neighbor
    }

    public double getWeight(LocationGraphNode neighbor) {
        return weights.getOrDefault(neighbor, Double.MAX_VALUE); // Return weight or max value if no edge exists
    }
}
