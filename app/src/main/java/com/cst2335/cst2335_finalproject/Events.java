package com.cst2335.cst2335_finalproject;

import java.io.Serializable;

/**
 * This class is for convert information between database and ArrayList
 * @atuthor Xiaojie Zhao
 */
public class Events implements Serializable {

    long _id;
    String eventName;
    String startDate;
    double minPrice;
    double maxPrice;
    String ticketMasterURL;
    String imgURL;

    public Events(long _id, String eventName, String startDate, double minPrice,
                  double maxPrice, String ticketMasterURL,String imgURL)
    {
        this._id=_id;
        this.eventName=eventName;
        this.startDate=startDate;
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
        this.ticketMasterURL=ticketMasterURL;
        this.imgURL=imgURL;
    }

    public long getId(){return _id;}
    public String getEventName(){return eventName;}
    public String getStartDate(){return startDate;}
    public double getMinPrice(){return minPrice;}
    public double getMaxPrice(){return maxPrice;}
    public String getTicketMasterURL(){return ticketMasterURL;}
    public String getImgURL(){return imgURL;}
}
