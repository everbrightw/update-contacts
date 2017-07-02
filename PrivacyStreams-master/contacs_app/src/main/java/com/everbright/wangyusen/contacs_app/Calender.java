package com.everbright.wangyusen.contacs_app;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.ActivityCompat;
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

    Calender(Context context) {
        this.purpose = Purpose.TEST("test");
        this.uqi = new UQI(context);
    }

    public static Intent addCalendar_event(int year, int month, int day, int time, int endtime, String event_title,
                                           String description, String location, String email) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month - 1, day, time / 100, time % 100);
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month - 1, day, endtime / 100, endtime % 100);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(Events.TITLE, event_title)
                .putExtra(Events.DESCRIPTION, description)
                .putExtra(Events.EVENT_LOCATION, location)
                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, email);
        return intent;
    }
    /*
     *@overload method with only event_time and event title
     */

    public static Intent addCalendar_event(int year, int month, int day, int time, int endtime, String event_title
    ) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month - 1, day, time / 100, time % 100);
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month - 1, day, endtime / 100, endtime % 100);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(Events.TITLE, event_title);

        return intent;
    }



    public void addEvents(ContentResolver cr, Context context,int year, int month, int day,
                     int time, int endtime, String event_title) {
        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month - 1, day, time / 100, time % 100);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month - 1, day, endtime / 100, endtime % 100);
        endMillis = endTime.getTimeInMillis();


        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, event_title);
        values.put(Events.DESCRIPTION, "Group workout");
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Uri uri = cr.insert(Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());

    }


    public int getCalendareventID( String original_title) throws PSException {
        String entryID =
                uqi.getData(CalendarEvent.getAll(), purpose).
                        filter("title", original_title).getItemAt(0).getField("id");
        int result = Integer.parseInt(entryID);
        return result;

    }

    public void updateEvents(ContentResolver cr, String newEvent_title, int eventID){
        String DEBUG_TAG = "MyActivity";

        //long eventID = 1503;//test id ;

        ContentValues values = new ContentValues();
        Uri updateUri = null;

        values.put(Events.TITLE, newEvent_title);//set the title
        /*
            values.put(Events.DTSTART, startMillis);
            values.put(Events.DTEND, endMillis);
            values.put(Events.TITLE, event_title);
            values.put(Events.DESCRIPTION, "Group workout");
            values.put(Events.CALENDAR_ID, calID);
            values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
        */

        updateUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
        int rows = cr.update(updateUri, values, null, null);
        Log.i(DEBUG_TAG, "Rows updated: " + rows);

    }

}
