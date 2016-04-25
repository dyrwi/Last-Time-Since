package com.dyrwi.lasttimesince.eventbus;

/**
 * Created by Ben on 21-Mar-16.
 */
public class ColorIconEvent extends BaseEvent{
    private int position;
    private int color;

    public ColorIconEvent(int position, int color) {
        this.position = position;
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
