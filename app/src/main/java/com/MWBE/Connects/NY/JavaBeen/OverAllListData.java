package com.MWBE.Connects.NY.JavaBeen;

/**
 * Created by Fazal on 9/28/2016.
 */
public class OverAllListData {
    String title;
    int TagID;

    public OverAllListData(String title, int tagID) {
        this.title = title;
        TagID = tagID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTagID() {
        return TagID;
    }

    public void setTagID(int tagID) {
        TagID = tagID;
    }
}
