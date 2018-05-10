package com.MWBE.Connects.NY.JavaBeen;

import java.io.Serializable;

/**
 * Created by Fazal on 5/13/2016.
 */
public class ListData_RFP implements Serializable {

    String id;
    String header;
    double rating;
    String title;
    String agency;
    String public_date;
    String due_date;
    String link;

    public ListData_RFP(String id, String header, double rating, String title, String agency, String public_date, String due_date) {
        this.id = id;
        this.header = header;
        this.rating = rating;
        this.title = title;
        this.agency = agency;
        this.public_date = public_date;
        this.due_date = due_date;
        //this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink (){
     return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public void setRating(int rating) {
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

    public String getPublic_date() {
        return public_date;
    }

    public void setPublic_date(String public_date) {
        this.public_date = public_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ListData_RFP other = (ListData_RFP) obj;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

}
