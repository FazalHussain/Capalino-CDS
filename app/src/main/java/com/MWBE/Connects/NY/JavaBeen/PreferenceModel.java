package com.MWBE.Connects.NY.JavaBeen;

/**
 * Created by fazal on 12/20/2017.
 */

public class PreferenceModel {
    int settingTypeID;
    int actualTagID;

    public PreferenceModel(int settingTypeID, int actualTagID) {
        this.settingTypeID = settingTypeID;
        this.actualTagID = actualTagID;
    }

    public int getSettingTypeID() {
        return settingTypeID;
    }

    public void setSettingTypeID(int settingTypeID) {
        this.settingTypeID = settingTypeID;
    }

    public int getActualTagID() {
        return actualTagID;
    }

    public void setActualTagID(int actualTagID) {
        this.actualTagID = actualTagID;
    }
}
