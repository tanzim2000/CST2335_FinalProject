package com.cst2335.cst2335_finalproject;

public class Event {

    private String eventName;
    private String startDate;
    private double minPrice;
    private double maxPrice;
    private String url;
    private String imagePath;

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getEventName() {
        return eventName;
    }

    public String getStartDate() {
        return startDate;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public String getUrl() {
        return url;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Event(String eventName, String startDate, double minPrice, double maxPrice, String url, String imagePath) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.url = url;
        this.imagePath = imagePath;
    }
}
