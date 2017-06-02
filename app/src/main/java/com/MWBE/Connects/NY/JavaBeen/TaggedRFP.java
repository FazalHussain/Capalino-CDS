package com.MWBE.Connects.NY.JavaBeen;

/**
 * Created by Fazal on 8/31/2016.
 */
public class TaggedRFP {

    int PreferenceID,ProcurementID,SettingTypeID,ActualTagID;
    String AddedDateTime,lastupdatedate;

    public TaggedRFP(int preferenceID, int procurementID, int settingTypeID, int actualTagID, String addedDateTime, String lastupdatedate) {
        PreferenceID = preferenceID;
        ProcurementID = procurementID;
        SettingTypeID = settingTypeID;
        ActualTagID = actualTagID;
        AddedDateTime = addedDateTime;
        this.lastupdatedate = lastupdatedate;
    }

    public int getPreferenceID() {
        return PreferenceID;
    }

    public void setPreferenceID(int preferenceID) {
        PreferenceID = preferenceID;
    }

    public int getProcurementID() {
        return ProcurementID;
    }

    public void setProcurementID(int procurementID) {
        ProcurementID = procurementID;
    }

    public int getSettingTypeID() {
        return SettingTypeID;
    }

    public void setSettingTypeID(int settingTypeID) {
        SettingTypeID = settingTypeID;
    }

    public int getActualTagID() {
        return ActualTagID;
    }

    public void setActualTagID(int actualTagID) {
        ActualTagID = actualTagID;
    }

    public String getAddedDateTime() {
        return AddedDateTime;
    }

    public void setAddedDateTime(String addedDateTime) {
        AddedDateTime = addedDateTime;
    }

    public String getLastupdatedate() {
        return lastupdatedate;
    }

    public void setLastupdatedate(String lastupdatedate) {
        this.lastupdatedate = lastupdatedate;
    }
}
