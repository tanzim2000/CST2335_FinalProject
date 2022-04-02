/*
 * @(#)Events.java Mar 27, 2022
 * Professor: Frank Emanuel
 * CST2335-012 Project
 * Students: Xiaojie Zhao, Shanshu Hong, Jun Fan
 */

package com.cst2335.cst2335_finalproject;

import java.io.Serializable;

/**
 * This class is for convert information between database and ArrayList
 * @atuthor Xiaojie Zhao
 */
public class Events implements Serializable {

    long _id;
    int isFavorite;
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
        this.isFavorite=isFavorite;
        this.eventName=eventName;
        this.startDate=startDate;
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
        this.ticketMasterURL=ticketMasterURL;
        this.imgURL=imgURL;
    }
    public Events(long _id, int isFavorite,String eventName, String startDate, double minPrice,
                  double maxPrice, String ticketMasterURL,String imgURL)
    {   this(_id, eventName, startDate, minPrice,maxPrice, ticketMasterURL,imgURL);
        this.isFavorite=isFavorite;

    }

    public long getId(){return _id;}
    public int getIsFavorite(){return isFavorite;}
    public String getEventName(){return eventName;}
    public String getStartDate(){return startDate;}
    public double getMinPrice(){return minPrice;}
    public double getMaxPrice(){return maxPrice;}
    public String getTicketMasterURL(){return ticketMasterURL;}
    public String getImgURL(){return imgURL;}
}
