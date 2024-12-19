package com.journeygenius.location_microservice.graph;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LocationGraph {
    // Map to store adjacency list and distances
    private Map<Integer, Map<Integer, Double>> adjacencyList = new HashMap<>();

    public void addLocation(Integer id) {
        adjacencyList.putIfAbsent(id, new HashMap<>());
    }

    public void addEdge(Integer fromId, Integer toId, double distance) {
        adjacencyList.putIfAbsent(fromId, new HashMap<>());
        adjacencyList.putIfAbsent(toId, new HashMap<>());
        adjacencyList.get(fromId).put(toId, distance); // Store distance for the edge
        // Optionally store the reverse edge if needed
        adjacencyList.get(toId).put(fromId, distance);
    }

    public List<Integer> getNeighbors(Integer id) {
        return new ArrayList<>(adjacencyList.getOrDefault(id, new HashMap<>()).keySet());
    }

    public void addLocationWithEdges(Integer id, List<Integer> connectedIds) {
        addLocation(id); // Add the location itself
        for (Integer connectedId : connectedIds) {
            addEdge(id, connectedId, 0); // Create edges to connected locations with default distance
        }
    }

    public void displayGraphinCLI() {
        System.out.println("Location Graph:");
        for (Map.Entry<Integer, Map<Integer, Double>> entry : adjacencyList.entrySet()) {
            Integer locationId = entry.getKey();
            Map<Integer, Double> neighbors = entry.getValue();

            StringBuilder output = new StringBuilder("Location " + locationId + " is connected to: ");

            if (neighbors.isEmpty()) {
                output.append("No connections.");
            } else {
                for (Map.Entry<Integer, Double> neighborEntry : neighbors.entrySet()) {
                    Integer neighborId = neighborEntry.getKey();
                    Double distance = neighborEntry.getValue();

                    if (distance != null) {
                        output.append("[").append(neighborId).append(": ").append(distance).append(" meters], ");
                    } else {
                        output.append("[").append(neighborId).append(": Not Calculated], ");
                    }
                }
                // Remove the trailing comma and space
                output.setLength(output.length() - 2);
            }

            System.out.println(output.toString());
        }
    }


    public String[] getConnectedPairs() {
        int maxPairs = 0;

        for (Map.Entry<Integer, Map<Integer, Double>> entry : adjacencyList.entrySet()) {
            Integer fromId = entry.getKey();
            Map<Integer, Double> neighbors = entry.getValue();

            for (Integer toId : neighbors.keySet()) {
                if (fromId < toId) { // To avoid duplicates
                    maxPairs++;
                }
            }
        }

        String[] connectedPairs = new String[maxPairs];
        int index = 0;

        for (Map.Entry<Integer, Map<Integer, Double>> entry : adjacencyList.entrySet()) {
            Integer fromId = entry.getKey();
            Map<Integer, Double> neighbors = entry.getValue();

            for (Integer toId : neighbors.keySet()) {
                if (fromId < toId) { // Only add if fromId is less than toId
                    connectedPairs[index++] = fromId + "," + toId;
                }
            }
        }

        return connectedPairs;
    }
}
