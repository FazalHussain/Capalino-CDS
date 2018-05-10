package com.MWBE.Connects.NY.JavaBeen;

import com.MWBE.Connects.NY.Database.isContentLoaded;

/**
 * Created by Fazal on 9/6/2016.
 */
public class ContentMasterUpdatedModel {
    int ContentID;

    String ContentTitle,ContentDescription,EventStartDateTime,EventEndDateTime,EventLocation,
            EventCost,ReferenceURL,ContentPostedDate,LASTUPDATE,ContentType;

    String ContentStatus;
    String Action;

    public ContentMasterUpdatedModel(int ContentID,String contentTitle, String contentDescription, String eventStartDateTime, String eventEndDateTime,
                                     String eventLocation, String eventCost, String referenceURL, String contentPostedDate,
                                     String LASTUPDATE, String contentType, String contentStatus, String action) {
        this.ContentID = ContentID;
        ContentTitle = contentTitle;
        ContentDescription = contentDescription;
        EventStartDateTime = eventStartDateTime;
        EventEndDateTime = eventEndDateTime;
        EventLocation = eventLocation;
        EventCost = eventCost;
        ReferenceURL = referenceURL;
        ContentPostedDate = contentPostedDate;
        this.LASTUPDATE = LASTUPDATE;
        ContentType = contentType;
        ContentStatus = contentStatus;
        Action = action;
    }

    public int getContentID() {
        return ContentID;
    }

    public void setContentID(int contentID) {
        ContentID = contentID;
    }

    public String getContentTitle() {
        return ContentTitle;
    }

    public void setContentTitle(String contentTitle) {
        ContentTitle = contentTitle;
    }

    public String getContentDescription() {
        return ContentDescription;
    }

    public void setContentDescription(String contentDescription) {
        ContentDescription = contentDescription;
    }

    public String getEventStartDateTime() {
        return EventStartDateTime;
    }

    public void setEventStartDateTime(String eventStartDateTime) {
        EventStartDateTime = eventStartDateTime;
    }

    public String getEventEndDateTime() {
        return EventEndDateTime;
    }

    public void setEventEndDateTime(String eventEndDateTime) {
        EventEndDateTime = eventEndDateTime;
    }

    public String getEventLocation() {
        return EventLocation;
    }

    public void setEventLocation(String eventLocation) {
        EventLocation = eventLocation;
    }

    public String getEventCost() {
        return EventCost;
    }

    public void setEventCost(String eventCost) {
        EventCost = eventCost;
    }

    public String getReferenceURL() {
        return ReferenceURL;
    }

    public void setReferenceURL(String referenceURL) {
        ReferenceURL = referenceURL;
    }

    public String getContentPostedDate() {
        return ContentPostedDate;
    }

    public void setContentPostedDate(String contentPostedDate) {
        ContentPostedDate = contentPostedDate;
    }

    public String getLASTUPDATE() {
        return LASTUPDATE;
    }

    public void setLASTUPDATE(String LASTUPDATE) {
        this.LASTUPDATE = LASTUPDATE;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getContentStatus() {
        return ContentStatus;
    }

    public void setContentStatus(String contentStatus) {
        ContentStatus = contentStatus;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }
}
