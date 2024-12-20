package com.journeygenius.location_microservice.data;


import java.util.*;

public class LocationGraph {
    private List<LocationGraphNode> nodes; // List of graph nodes


    public LocationGraph() {
        this.nodes = new ArrayList<>(); // Initialize the node list
    }

    // Add a new node to the graph
    public void addNode(Location location) {
        nodes.add(new LocationGraphNode(location)); // Create a new node and add it to the list
    }

    // Add an edge between two nodes (bi-directional)
    public void addEdge(Location from, Location to, double weight) {
        LocationGraphNode fromNode = findNode(from);
        LocationGraphNode toNode = findNode(to);

        if (fromNode != null && toNode != null) {
            fromNode.addNeighbor(toNode, weight); // Add to neighbors of fromNode with weight
            toNode.addNeighbor(fromNode, weight); // Add to neighbors of toNode (bi-directional)
        }
    }

    // Find a node in the graph by its location
    public LocationGraphNode findNode(Location location) {
        for (LocationGraphNode node : nodes) {
            if (node.getLocation().getId().equals(location.getId())) { // Assuming ID is unique
                return node; // Return the found node
            }
        }
        return null; // Node not found
    }

    public List<LocationGraphNode> getNodes() {
        return nodes;
    }
}
