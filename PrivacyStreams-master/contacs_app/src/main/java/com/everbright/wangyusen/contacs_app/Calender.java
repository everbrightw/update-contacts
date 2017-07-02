package com.everbright.wangyusen.contacs_app;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.github.privacystreams.calendar.CalendarEvent;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.purposes.Purpose;
import java.util.Calendar;


/**
 * Created by wangyusen on 7/1/17.
 */

public class Calender {

    /**
     * @param  year eg; 2014
     * @param  month eg: january 1
     * @param  day the day this event starts
     * @param  time the time this event starts / four or three digits number 730 equals 7:30
     * @param  endtime the time this event ends
     */
    private UQI uqi;
    private Purpose purpose;
    Calender(Context context){
        this.purpose = Purpose.TEST("test");
        this.uqi = new UQI(context);
    }

    public static Intent addCalendar_event(int year, int month, int day, int time, int endtime, String event_title,
                                    String description, String location, String email){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month-1, day, time/100, time%100);
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month-1, day, endtime/100, endtime%100);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, event_title)
                .putExtra(CalendarContract.Events.DESCRIPTION, description)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, email);
        return intent;
    }
    /*
     *@overload method with only event_time and event title
     */

    public static Intent addCalendar_event(int year, int month, int day, int time, int endtime, String event_title
                                   ){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month-1, day, time/100, time%100);
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month-1, day, endtime/100, endtime%100);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, event_title);

        return intent;
    }
    public String getCalendareventID( String original_title) throws PSException {
        String entryID =
                uqi.getData(CalendarEvent.getAll(), purpose).filter("title", original_title).getItemAt(0).getField("id");
        return entryID;

    }
    public static Intent UpdateCalendarEntry() {
        long eventID = 1503;

        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        Intent intent = new Intent(Intent.ACTION_EDIT)
                .setData(uri)
                .putExtra(CalendarContract.Events.TITLE,"new_title");
        return intent;

    }






}
