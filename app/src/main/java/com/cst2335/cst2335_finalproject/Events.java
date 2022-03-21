package com.cst2335.cst2335_finalproject;

import java.util.Date;

public class Events {

    long id;
    String eventName;
    Date startDate;
    double minPrice;
    double maxPrice;
    String ticketMasterURL;

    public Events(int id, String eventName, String ticketMasterURL) {
        this.id=id;
        this.eventName=eventName;
        this.ticketMasterURL=ticketMasterURL;
    }

    public void Evnets(long id, String eventName, Date startDate,
                       double minPrice, double maxPrice, String ticketMasterURL)
    {
        this.id=id;

        this.eventName=eventName;
        this.startDate=startDate;
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
        this.ticketMasterURL=ticketMasterURL;

    }

    public long getId(){return id;}
    public String getEventName(){return eventName;}
    public Date getStartDate(){return startDate;}
    public double getMinPrice(){return minPrice;}
    public double getMaxPrice(){return maxPrice;}
    public String getTicketMasterURL(){return ticketMasterURL;}
}
