package com.journeygenius.location_microservice.controller;

import com.journeygenius.location_microservice.data.DistanceInfo;
import com.journeygenius.location_microservice.data.Location;
import com.journeygenius.location_microservice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/search")
    public ResponseEntity<Location> getLocationByName(@RequestParam String name) {
        Location location = locationService.getLocationByName(name);
        if (location != null) {
            return ResponseEntity.ok(location);
        }
        return ResponseEntity.notFound().build(); // Return 404 if not found
    }

    @PostMapping("/")
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        Location createdLocation = locationService.addLocation(location);
        return ResponseEntity.ok(createdLocation);
    }

    @GetMapping("/")
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @PostMapping("/add-to-graph")
    public ResponseEntity<String> addLocationsToGraph() {
        locationService.addLocationsToGraph();
        return ResponseEntity.ok("All locations added to the graph.");
    }

    @PostMapping("/create-edges")
    public ResponseEntity<String> createEdgesBetweenNodes() {
        locationService.createEdgesBetweenNodes();
        return ResponseEntity.ok("Edges created between all nodes in the graph.");
    }

    @GetMapping("/calculate-distances")
    public ResponseEntity<List<DistanceInfo>> getDistances() {
        List<DistanceInfo> distances = locationService.calculateDistances();

        if (distances != null && !distances.isEmpty()) {
            return ResponseEntity.ok(distances); // Return distances as JSON
        }

        return ResponseEntity.noContent().build(); // Return 204 if no distances found
    }

    @PostMapping("/set-current")
    public ResponseEntity<String> setCurrentLocation(@RequestBody Location location) {
        locationService.setCurrentLocation(location);
        return ResponseEntity.ok("Current location set successfully.");
    }

    @GetMapping("/optimal-path")
    public ResponseEntity<List<Location>> calculateOptimalPath(@RequestParam Integer sourceId) {
        List<Location> optimalPath = locationService.calculateOptimalPath(sourceId);

        if (!optimalPath.isEmpty()) {
            return ResponseEntity.ok(optimalPath); // Return the optimal path as JSON
        }

        return ResponseEntity.notFound().build(); // Return 404 if no path found or unreachable
    }


    @GetMapping("/find-id")
    public ResponseEntity<Integer> getIdByName(@RequestParam String name) {
        Integer id = locationService.findIdByName(name);
        if (id != null) {
            return ResponseEntity.ok(id); // Return the found ID
        }
        return ResponseEntity.notFound().build(); // Return 404 if not found
    }
}
