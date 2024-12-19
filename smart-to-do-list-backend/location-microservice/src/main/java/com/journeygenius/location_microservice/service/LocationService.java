package com.journeygenius.location_microservice.service;

import com.journeygenius.location_microservice.data.DistanceInfo;
import com.journeygenius.location_microservice.data.Location;
import com.journeygenius.location_microservice.data.LocationRepository;
import com.journeygenius.location_microservice.graph.LocationGraph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationGraph locationGraph;

    private final String SEARCH_API_URL = "https://us1.locationiq.com/v1/search?";
    private final String DISTANCE_MATRIX_API_URL = "https://us1.locationiq.com/v1/directions/driving/";
    private final String API_KEY = "pk.eaeb7461e0e73e6e0ef1e22d4255b1de";

        public Location getLocationDetails(String query) {
            RestTemplate restTemplate = new RestTemplate();

            String url = String.format("%skey=%s&q=%s&format=JSON", SEARCH_API_URL, API_KEY, query);

            String jsonString = restTemplate.getForObject(url, String.class);
            if (jsonString != null && jsonString.startsWith("[")) {
                jsonString = jsonString.substring(1, jsonString.length() - 1);

                String[] pairs = jsonString.substring(1, jsonString.length() - 1).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Split by comma not inside quotes
                String displayName = "";
                double lon = 0.0;
                double lat = 0.0;

                for (String pair : pairs) {
                    String[] keyValue = pair.split(":", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim().replace("\"", "");
                        String value = keyValue[1].trim().replace("\"", "");
                        switch (key) {
                            case "display_name":
                                displayName = value;
                                break;
                            case "lon":
                                lon = Double.parseDouble(value);
                                break; // Convert to double
                            case "lat":
                                lat = Double.parseDouble(value);
                                break; // Convert to double
                        }
                    }
                }

                // Create and return a Location object
                Location location = new Location();
                location.setName(displayName);
                location.setLongitude(lon);
                location.setLatitude(lat);

                return location;
            }
            return null;

        }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    public void addLocationToGraph(Integer id, List<Integer> connectedIds) {
        locationGraph.addLocationWithEdges(id, connectedIds);
    }

    public void showGraphinCLI(){
        locationGraph.displayGraphinCLI();
    }

    public List<DistanceInfo> calculateDistance() {
        List<DistanceInfo> distances = new ArrayList<>();
        String[] connectedPairs = locationGraph.getConnectedPairs();

        for (String pair : connectedPairs) {
            String[] ids = pair.split(",");
            Integer fromId = Integer.valueOf(ids[0]);
            Integer toId = Integer.valueOf(ids[1]);

            // Fetch coordinates by ID
            String[] fromCoordinates = locationRepository.getCoordinatesById(fromId);
            String[] toCoordinates = locationRepository.getCoordinatesById(toId);

            if (fromCoordinates != null && toCoordinates != null) {
                Double fromLongitude = Double.valueOf((fromCoordinates[0]).split(",")[0]);
                Double fromLatitude = Double.valueOf((fromCoordinates[0]).split(",")[1]);
                Double toLongitude = Double.valueOf((toCoordinates[0]).split(",")[0]);
                Double toLatitude = Double.valueOf((toCoordinates[0]).split(",")[1]);

                RestTemplate restTemplate = new RestTemplate();
                String url = String.format("%s%f,%f;%f,%f?key=%s&overview=simplified&annotations=false",
                        DISTANCE_MATRIX_API_URL, fromLongitude, fromLatitude, toLongitude, toLatitude, API_KEY);

                String jsonString = restTemplate.getForObject(url, String.class);
                String[] parts = jsonString.split("\"distance\":");

                if (parts.length > 1) {
                    String distancePart = parts[1].split(",")[0];
                    distancePart = distancePart.replaceAll("[^0-9.]", "");

                    try {
                        double distance = Double.parseDouble(distancePart);
                        distances.add(new DistanceInfo(fromId, toId, distance));
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing distance: " + e.getMessage());
                    }
                } else {
                    System.out.println("Distance not found in JSON.");
                }
            } else {
                System.out.println("Location not found for IDs: " + fromId + " or " + toId);
            }
        }
        return distances;
    }

    public void addLocationToGraph(int fromId, int toId, double distance) {
        locationGraph.addEdge(fromId, toId, distance);
    }




}
