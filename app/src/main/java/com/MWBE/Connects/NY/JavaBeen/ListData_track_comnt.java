package com.MWBE.Connects.NY.JavaBeen;

/**
 * Created by Fazal on 5/30/2016.
 */
public class ListData_track_comnt {
    String title;
    int image;

    public ListData_track_comnt(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public ListData_track_comnt() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
