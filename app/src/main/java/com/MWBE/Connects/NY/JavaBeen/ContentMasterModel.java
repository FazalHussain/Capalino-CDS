package com.MWBE.Connects.NY.JavaBeen;

/**
 * Created by Fazal on 6/30/2016.
 */
public class ContentMasterModel {

    String ContentType, ContentTitle, ContentShortDescription, ContentLongDescription, ContentReferenceURL, ContentPostedByUser,
            ContentPostedDate, ContentStatusCode, ContentExpirationDate, ContentRelevantDateTime, LASTUPDATE;

    public ContentMasterModel() {
    }

    public ContentMasterModel(String contentType, String contentTitle, String contentShortDescription,
                              String contentLongDescription, String contentReferenceURL, String contentPostedByUser,
                              String contentPostedDate, String contentStatusCode, String contentExpirationDate,
                              String contentRelevantDateTime, String LASTUPDATE) {
        ContentType = contentType;
        ContentTitle = contentTitle;
        ContentShortDescription = contentShortDescription;
        ContentLongDescription = contentLongDescription;
        ContentReferenceURL = contentReferenceURL;
        ContentPostedByUser = contentPostedByUser;
        ContentPostedDate = contentPostedDate;
        ContentStatusCode = contentStatusCode;
        ContentExpirationDate = contentExpirationDate;
        ContentRelevantDateTime = contentRelevantDateTime;
        this.LASTUPDATE = LASTUPDATE;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getContentTitle() {
        return ContentTitle;
    }

    public void setContentTitle(String contentTitle) {
        ContentTitle = contentTitle;
    }

    public String getContentShortDescription() {
        return ContentShortDescription;
    }

    public void setContentShortDescription(String contentShortDescription) {
        ContentShortDescription = contentShortDescription;
    }

    public String getContentLongDescription() {
        return ContentLongDescription;
    }

    public void setContentLongDescription(String contentLongDescription) {
        ContentLongDescription = contentLongDescription;
    }

    public String getContentReferenceURL() {
        return ContentReferenceURL;
    }

    public void setContentReferenceURL(String contentReferenceURL) {
        ContentReferenceURL = contentReferenceURL;
    }

    public String getContentPostedByUser() {
        return ContentPostedByUser;
    }

    public void setContentPostedByUser(String contentPostedByUser) {
        ContentPostedByUser = contentPostedByUser;
    }

    public String getContentPostedDate() {
        return ContentPostedDate;
    }

    public void setContentPostedDate(String contentPostedDate) {
        ContentPostedDate = contentPostedDate;
    }

    public String getContentStatusCode() {
        return ContentStatusCode;
    }

    public void setContentStatusCode(String contentStatusCode) {
        ContentStatusCode = contentStatusCode;
    }

    public String getContentExpirationDate() {
        return ContentExpirationDate;
    }

    public void setContentExpirationDate(String contentExpirationDate) {
        ContentExpirationDate = contentExpirationDate;
    }

    public String getContentRelevantDateTime() {
        return ContentRelevantDateTime;
    }

    public void setContentRelevantDateTime(String contentRelevantDateTime) {
        ContentRelevantDateTime = contentRelevantDateTime;
    }

    public String getLASTUPDATE() {
        return LASTUPDATE;
    }

    public void setLASTUPDATE(String LASTUPDATE) {
        this.LASTUPDATE = LASTUPDATE;
    }
}
