package com.example.disasterfasoulisproject.Model;

public class Earthquake {
    private String place;
    private double magnitude;
    private String offsetlocation;
    private String primarylocation;
    private long time;
    private String detailLink;
    private String type;
    private double lat;
    private double lot;

    public Earthquake() {
    }

    public Earthquake(double magnitude, long time, String detailLink, String type, double lat, double lot) {
        this.magnitude = magnitude;
        this.time = time;
        this.detailLink = detailLink;
        this.type = type;
        this.lat = lat;
        this.lot = lot;
    }

    public Earthquake(String place, double magnitude, long time, String detailLink, String type, double lat, double lot) {
        this.place = place;
        this.magnitude = magnitude;
        this.time = time;
        this.detailLink = detailLink;
        this.type = type;
        this.lat = lat;
        this.lot = lot;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLot() {
        return lot;
    }

    public void setLot(double lot) {
        this.lot = lot;
    }
}
