package com.MWBE.Connects.NY.SectionAdapter;

/**
 * Created by Fazal on 7/29/2016.
 */
public class SectionItem implements Item {

    private final String title;

    public SectionItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean isSection() {
        return true;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof SectionItem)
        {
            sameSame = this.title == ((SectionItem) object).title;
        }

        return sameSame;
    }
}
