package com.MWBE.Connects.NY.JavaBeen;

/**
 * Created by Fazal on 5/10/2016.
 */
public class ListData {
    int image;
    String text;
    String time;

    public ListData(int image, String text, String time) {
        this.image = image;
        this.text = text;
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
