package com.MWBE.Connects.NY.JavaBeen;

import java.io.Serializable;

/**
 * Created by Fazal on 5/13/2016.
 */
public class ListData_Agency implements Serializable {
    String title;
    boolean ischecked;

    public ListData_Agency(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }
}
