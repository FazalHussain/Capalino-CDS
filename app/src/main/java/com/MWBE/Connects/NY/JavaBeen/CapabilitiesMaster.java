package com.MWBE.Connects.NY.JavaBeen;

/**
 * Created by fazal on 2/2/2017.
 */

public class CapabilitiesMaster {
    int TagID;
    int CapTagID;
    int TagValueID;
    String TagValueTitle;

    public CapabilitiesMaster(int tagID, int capTagID, int tagValueID, String tagValueTitle) {
        TagID = tagID;
        CapTagID = capTagID;
        TagValueID = tagValueID;
        TagValueTitle = tagValueTitle;
    }

    public int getTagID() {
        return TagID;
    }

    public void setTagID(int tagID) {
        TagID = tagID;
    }

    public int getCapTagID() {
        return CapTagID;
    }

    public void setCapTagID(int capTagID) {
        CapTagID = capTagID;
    }

    public int getTagValueID() {
        return TagValueID;
    }

    public void setTagValueID(int tagValueID) {
        TagValueID = tagValueID;
    }

    public String getTagValueTitle() {
        return TagValueTitle;
    }

    public void setTagValueTitle(String tagValueTitle) {
        TagValueTitle = tagValueTitle;
    }
}
