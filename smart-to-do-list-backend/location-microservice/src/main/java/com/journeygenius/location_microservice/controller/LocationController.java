package com.journeygenius.location_microservice.controller;

import com.journeygenius.location_microservice.data.DistanceInfo;
import com.journeygenius.location_microservice.data.Location;
import com.journeygenius.location_microservice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping(path = "/locations")
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @PostMapping(path = "/locations")
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        return ResponseEntity.ok(locationService.saveLocation(location));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addLocationToGraph(@RequestParam Integer id, @RequestParam List<Integer> connectedIds) {
        locationService.addLocationToGraph(id, connectedIds);

        return ResponseEntity.ok("Location added to the graph successfully.");
    }

    @GetMapping("/display")
    public ResponseEntity<String> displayGraph() {
        locationService.showGraphinCLI();
        return ResponseEntity.ok("Graph displayed in CLI.");
    }

    @GetMapping("/distance")
    public ResponseEntity<String> calculateDistance() {
        List<DistanceInfo> distances = locationService.calculateDistance();

        // Check if distances were calculated
        if (distances.isEmpty()) {
            return ResponseEntity.ok("No distances calculated.");
        }

        // Insert distances into the LocationGraph
        for (DistanceInfo info : distances) {
            locationService.addLocationToGraph(info.getFromId(), info.getToId(), info.getDistance());
        }

        return ResponseEntity.ok("Distances calculated and inserted successfully.");
    }

    @GetMapping(path = "/location-information")
    public Location getLocationDetails(@RequestParam String query){
        Location locationDetails = locationService.getLocationDetails(query);
        return locationDetails;
    }
}
