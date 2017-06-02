package com.MWBE.Connects.NY.JavaBeen;

/**
 * Created by Fazal on 9/6/2016.
 */
public class ContentMasterUpdatedModel {
    String ContentTitle,ContentDescription,EventStartDateTime,EventEndDateTime,EventLocation,
            EventCost,ReferenceURL,ContentPostedDate,LASTUPDATE,ContentType;

    public ContentMasterUpdatedModel(String contentTitle, String contentDescription, String eventStartDateTime,
                                     String eventEndDateTime, String eventLocation, String eventCost, String referenceURL,
                                     String contentPostedDate, String LASTUPDATE, String contentType) {
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
}
