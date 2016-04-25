package com.dyrwi.lasttimesince.eventbus;

/**
 * Created by Ben on 15-Mar-16.
 *
 * Created for use with EventBus.
 * This is used fo the DateDialogFragment file.
 */
public class DateDialogEvent extends BaseEvent {
    int year;
    int monthOfYear;
    int dayOfMonth;

    public DateDialogEvent(int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public void setMonthOfYear(int monthOfYear) {
        this.monthOfYear = monthOfYear;
    }



}
