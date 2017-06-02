package com.MWBE.Connects.NY.JavaBeen;

import java.io.Serializable;

/**
 * Created by Fazal on 5/30/2016.
 */
public class ListData_Track implements Serializable {
    String id;
    String header;
    double rating;
    String title;
    String agency;
    String track_started_date;

    public ListData_Track(String id, String header, double rating, String title, String agency, String track_started_date) {
        this.id = id;
        this.header = header;
        this.rating = rating;
        this.title = title;
        this.agency = agency;
        this.track_started_date = track_started_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getTrack_started_date() {
        return track_started_date;
    }

    public void setTrack_started_date(String track_started_date) {
        this.track_started_date = track_started_date;
    }
}
