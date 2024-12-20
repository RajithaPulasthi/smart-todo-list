package com.journeygenius.location_microservice.service;

import com.journeygenius.location_microservice.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    private LocationGraph locationGraph = new LocationGraph(); // Graph to manage locations

    private final String SEARCH_API_URL = "https://us1.locationiq.com/v1/search?";
    private final String DISTANCE_MATRIX_API_URL = "https://us1.locationiq.com/v1/directions/driving/";
    private final String API_KEY = "pk.eaeb7461e0e73e6e0ef1e22d4255b1de";

    private Location currentLocation;

    // Method to get location details based on name using LocationIQ API
    public Location getLocationByName(String name) {
        String url = String.format("%skey=%s&q=%s&format=json", SEARCH_API_URL, API_KEY, name);
        RestTemplate restTemplate = new RestTemplate();
        Object[] locations = restTemplate.getForObject(url, Object[].class);

        if (locations != null && locations.length > 0) {
            Object firstResult = locations[0];
            String displayName = ((Map<String, Object>) firstResult).get("display_name").toString();
            Double lon = Double.valueOf(((Map<String, Object>) firstResult).get("lon").toString());
            Double lat = Double.valueOf(((Map<String, Object>) firstResult).get("lat").toString());
            return new Location(displayName, lon, lat); // Create and return a new Location object
        }
        return null; // Return null if no location found
    }

    // Method to add a location to the database
    public Location addLocation(Location location) {
        return locationRepository.save(location); // Save to database and return saved entity
    }

    // Method to retrieve all locations from the database
    public List<Location> getAllLocations() {
        return locationRepository.findAll(); // Fetch all locations from the database
    }

    // Method to add all locations to the graph nodes
    public void addLocationsToGraph() {
        List<Location> locations = getAllLocations();
        for (Location loc : locations) {
            locationGraph.addNode(loc); // Add each location as a node in the graph
        }
    }

    // Method to create edges between all nodes in the graph
    public void createEdgesBetweenNodes() {
        List<Location> locations = getAllLocations();
        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                // Assuming you have a method to calculate distance between two locations
                double distance = calculateDistance(locations.get(i), locations.get(j));
                locationGraph.addEdge(locations.get(i), locations.get(j), distance); // Create edges with weights
            }
        }
    }

    // Method to calculate distances between two locations using LocationIQ Driving API
    private double calculateDistance(Location from, Location to) {
        String url = String.format("%s%f,%f;%f,%f?key=%s&overview=simplified&annotations=false",
                DISTANCE_MATRIX_API_URL,
                from.getLongitude(),
                from.getLatitude(),
                to.getLongitude(),
                to.getLatitude(),
                API_KEY);

        RestTemplate restTemplate = new RestTemplate();
        try {
            DistanceResponse response = restTemplate.getForObject(url, DistanceResponse.class);
            if (response != null && response.getRoutes() != null && !response.getRoutes().isEmpty()) {
                return response.getRoutes().get(0).getDistance(); // Return distance from first route
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 429) {
                System.out.println("Rate limit exceeded. Waiting for a second...");
                try {
                    TimeUnit.SECONDS.sleep(1); // Wait for 1 second before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                }
            } else {
                throw e; // Rethrow other exceptions
            }
        }

        return Double.MAX_VALUE; // Return max value if no valid distance found
    }

    // Method to calculate distances between each node using LocationIQ Driving API
    public List<DistanceInfo> calculateDistances() {
        List<Location> locations = getAllLocations();
        List<DistanceInfo> distances = new ArrayList<>(); // List to hold distance info

        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                // Construct the URL for the API request
                String url = String.format("%s%f,%f;%f,%f?key=%s&overview=simplified&annotations=false",
                        DISTANCE_MATRIX_API_URL,
                        locations.get(i).getLongitude(), // Corrected to use longitude here
                        locations.get(i).getLatitude(),
                        locations.get(j).getLongitude(), // Corrected to use longitude here
                        locations.get(j).getLatitude(),
                        API_KEY);

                RestTemplate restTemplate = new RestTemplate();
                try {
                    // Send the request and get the response
                    DistanceResponse response = restTemplate.getForObject(url, DistanceResponse.class);
                    if (response != null && response.getRoutes() != null && !response.getRoutes().isEmpty()) {
                        double distance = response.getRoutes().get(0).getDistance(); // Accessing the first route's distance
                        distances.add(new DistanceInfo(locations.get(i).getId(), locations.get(j).getId(), distance));
                    }

                } catch (HttpClientErrorException e) {
                    if (e.getStatusCode().value() == 429) {
                        System.out.println("Rate limit exceeded. Waiting for a second...");
                        try {
                            TimeUnit.SECONDS.sleep(1); // Wait for 1 second before retrying
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt(); // Restore interrupted status
                        }
                    } else {
                        throw e; // Rethrow other exceptions
                    }
                }

                // Throttle to ensure we do not exceed 2 requests per second
                try {
                    TimeUnit.MILLISECONDS.sleep(500); // Sleep for 500 milliseconds (2 requests per second)
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                }
            }
        }

        return distances; // Return the list of distances
    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public List<Location> calculateOptimalPath(Integer sourceId) {
        List<LocationGraphNode> nodes = locationGraph.getNodes(); // Get all nodes from the graph
        Map<LocationGraphNode, Double> distances = new HashMap<>();
        Map<LocationGraphNode, LocationGraphNode> previousNodes = new HashMap<>();
        PriorityQueue<LocationGraphNode> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        Location source = getLocationById(sourceId);
        if (source == null) {
            return Collections.emptyList(); // Return empty if source not found
        }

        LocationGraphNode startNode = locationGraph.findNode(source);
        if (startNode == null) {
            return Collections.emptyList(); // Return empty if source node not found in graph
        }

        // Initialize distances for all nodes
        for (LocationGraphNode node : nodes) {
            distances.put(node, Double.MAX_VALUE); // Set all distances to infinity
            previousNodes.put(node, null); // No previous node at start
            priorityQueue.add(node); // Add all nodes to the priority queue
        }

        distances.put(startNode, 0.0); // Distance from start node to itself is zero

        while (!priorityQueue.isEmpty()) {
            LocationGraphNode current = priorityQueue.poll(); // Get node with smallest distance

            for (LocationGraphNode neighbor : current.getNeighbors()) {
                double edgeWeight = current.getWeight(neighbor);
                double newDistance = distances.get(current) + edgeWeight;

                if (newDistance < distances.get(neighbor)) { // If found shorter path
                    distances.put(neighbor, newDistance);
                    previousNodes.put(neighbor, current);

                    // Re-add neighbor with updated distance in priority queue
                    priorityQueue.remove(neighbor);
                    priorityQueue.add(neighbor);
                }
            }
        }

        // Collecting all reachable locations in order of distance from the source
        List<Location> orderedLocations = new ArrayList<>();

        for (LocationGraphNode node : nodes) {
            if (distances.get(node) < Double.MAX_VALUE) { // Only include reachable nodes
                orderedLocations.add(node.getLocation());
            }
        }

        // Sort by distance only for reachable locations
        orderedLocations.sort(Comparator.comparingDouble(location -> {
            Double distance = distances.get(locationGraph.findNode(location));
            return (distance != null) ? distance : Double.MAX_VALUE; // Handle potential nulls safely
        }));

        return orderedLocations; // Return the ordered list of locations based on shortest distance from source
    }



    public Integer findIdByName(String name) {
        Location location = locationRepository.findByName(name);
        return (location != null) ? location.getId() : null; // Return ID or null if not found
    }

    public Location getLocationById(Integer id) {
        return locationRepository.findById(id).orElse(null); // Fetch location by ID, return null if not found
    }


}
