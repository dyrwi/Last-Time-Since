package com.dyrwi.lasttimesince.eventbus;

/**
 * Created by Ben on 15-Mar-16.
 *
 * Created for EventBus
 * For use with the TimeDialogFragment class.
 */
public class TimeDialogEvent extends BaseEvent{
    private int hourOfDay;
    private int minute;

    public TimeDialogEvent(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
