package com.journeygenius.location_microservice.data;

public class DistanceInfo {
    private Integer fromId;
    private Integer toId;
    private double distance;

    public DistanceInfo(Integer fromId, Integer toId, double distance) {
        this.fromId = fromId;
        this.toId = toId;
        this.distance = distance;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
