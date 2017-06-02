package com.MWBE.Connects.NY.JavaBeen;

/**
 * Created by Fazal on 5/27/2016.
 */
public class ListData_Resource {
    String text;
    int image;

    public ListData_Resource(String text, int image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
