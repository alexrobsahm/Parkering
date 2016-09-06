package com.robsahm.parking.util;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.widget.Toast;

import com.robsahm.parking.R;
import com.robsahm.parking.util.json.Property;

import java.util.Calendar;

/**
 * Created by alexrobsahm on 17/08/16.
 */
public class CalendarUtil {

    public static Intent getCalendarIntent(Context context, Property property) {
        Calendar start = CalendarUtil.getStart(property);
        Calendar end = CalendarUtil.getEnd(property);

        if (start == null || end == null) {
            ToastUtil.getInstance(context).show(R.string.weekday_error,
                    Toast.LENGTH_SHORT);
            return null;
        } else if (end.before(start)) {
            ToastUtil.getInstance(context).show(R.string.cleaning_in_progress,
                    Toast.LENGTH_SHORT);
            return null;
        }

        return createCalendarIntent(context, property, start, end);
    }

    private static Calendar getEnd(Property property) {
        return createCalendar(property, property.getStartTime());
    }

    private static Calendar getStart(Property property) {
        return createCalendar(property, property.getEndTime());
    }

    private static Calendar createCalendar(Property property, int time) {
        Calendar calendar = Calendar.getInstance();
        int weekday = getWeekday(property);

        if (weekday == -1) {
            return null;
        }

        while (calendar.get(Calendar.DAY_OF_WEEK) != weekday) {
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        calendar.set(Calendar.HOUR_OF_DAY, time/100);
        calendar.set(Calendar.MINUTE, time%100);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_WEEK, 7);
        }

        return calendar;
    }

    private static int getWeekday(Property property) {
        switch (property.getStartWeekday()) {
            case "måndag":
                return Calendar.MONDAY;
            case "tisdag":
                return Calendar.TUESDAY;
            case "onsdag":
                return Calendar.WEDNESDAY;
            case "torsdag":
                return Calendar.THURSDAY;
            case "fredag":
                return Calendar.FRIDAY;
            case "lördag":
                return Calendar.SATURDAY;
            case "söndag":
                return Calendar.SUNDAY;
            default:
                return -1;
        }
    }

    private static Intent createCalendarIntent(
            Context context,
            Property property,
            Calendar start,
            Calendar end) {
        String reminder_text = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("settings_reminder_text", "");
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
        calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
        calendarIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        calendarIntent.putExtra(CalendarContract.Events.TITLE, reminder_text);
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, property.getAddress());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end.getTimeInMillis());

        return calendarIntent;
    }
}
