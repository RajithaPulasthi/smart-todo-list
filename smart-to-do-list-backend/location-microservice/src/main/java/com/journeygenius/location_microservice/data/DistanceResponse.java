package com.journeygenius.location_microservice.data;

import java.util.List;

public class DistanceResponse {
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public static class Route {
        private double distance; // Distance in meters

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}
