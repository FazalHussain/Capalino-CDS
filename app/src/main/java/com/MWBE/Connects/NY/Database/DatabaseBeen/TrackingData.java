package com.MWBE.Connects.NY.Database.DatabaseBeen;

/**
 * Created by Fazal on 6/9/2016.
 */
public class TrackingData {

    String ProcurementTitle;
    String AgencyTitle;
    String TrackDate;
    String ProposalDeadLine;
    String UserID;
    double Rating;
    String ProcID;

    public TrackingData(String procurementTitle, String agencyTitle, String trackDate, String proposalDeadLine, String userID, double rating, String procID) {
        ProcurementTitle = procurementTitle;
        AgencyTitle = agencyTitle;
        TrackDate = trackDate;
        ProposalDeadLine = proposalDeadLine;
        UserID = userID;
        Rating = rating;
        ProcID = procID;
    }

    public String getProcID() {
        return ProcID;
    }

    public void setProcID(String procID) {
        ProcID = procID;
    }

    public String getProcurementTitle() {
        return ProcurementTitle;
    }

    public void setProcurementTitle(String procurementTitle) {
        ProcurementTitle = procurementTitle;
    }

    public String getAgencyTitle() {
        return AgencyTitle;
    }

    public void setAgencyTitle(String agencyTitle) {
        AgencyTitle = agencyTitle;
    }

    public String getTrackDate() {
        return TrackDate;
    }

    public void setTrackDate(String trackDate) {
        TrackDate = trackDate;
    }

    public String getProposalDeadLine() {
        return ProposalDeadLine;
    }

    public void setProposalDeadLine(String proposalDeadLine) {
        ProposalDeadLine = proposalDeadLine;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }
}
