package com.MWBE.Connects.NY.SectionAdapter;

/**
 * Created by Fazal on 7/29/2016.
 */
public class EntryItem implements Item {

    String title;
    int tagvalueID;
    boolean ischecked;

    public EntryItem(String title,int tagvalueID) {
        this.title = title;
        this.tagvalueID = tagvalueID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTagvalueID() {
        return tagvalueID;
    }

    public void setTagvalueID(int tagvalueID) {
        this.tagvalueID = tagvalueID;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}
