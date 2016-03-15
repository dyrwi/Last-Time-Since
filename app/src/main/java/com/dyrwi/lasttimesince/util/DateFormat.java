package com.dyrwi.lasttimesince.util;

import android.content.Context;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.Date;

/**
 * Created by Ben on 09-Mar-16.
 */
public class DateFormat {

    private DateFormat(){}

    public static String lastTimeSince(Context context, Date date) {
        DateTime currentDate = DateTime.now();
        DateTime oldDate = new DateTime(date);
        Interval interval = new Interval(oldDate, currentDate);
        String s = DateUtils.formatElapsedTime(interval.toDuration());
        DateUtils.formatDuration(context, interval.toDuration());

        //int p = new Period(LocalDate.now(), LocalDate.fromDateFields(date), PeriodType.days()).getDays();
        return s;

    }
}
