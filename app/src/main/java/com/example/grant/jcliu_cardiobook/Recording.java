package com.example.grant.jcliu_cardiobook;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Made by Grant Liu, 30/01/2019
 */
public class Recording {
    private Date date;
    private int systolic;
    private int diastolic;
    private int heartRate;
    private String comment;

    /**
     * default constructor, sets date to current time
     */
    public Recording() {
        this.date = Calendar.getInstance().getTime();
    }

    /**
     * @return Current date stored in class
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @param context TODO
     * @return formatted string date in yyyy-mm-d HH-mm (24 hr)
     */
    public String getDateFormatted(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm",
                context.getResources().getConfiguration().locale);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(this.date);
    }

    /**
     *
     * @param year year to set represented in integer
     * @param month month to be set as an integer
     * @param day day to be set as an integer
     * @param hour 24 hour time format
     * @param min minute
     */
    public void setDate(int year, int month, int day, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, min);
        this.date = calendar.getTime();
    }

    /**
     *
     * @param systolic integer representing systolic pressure
     */
    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    /**
     * @return stored systolic pressure
     */
    public int getSystolic() {
        return systolic;
    }

    /**
     *
     * @return stored diastolic pressure
     */
    public int getDiastolic() {
        return diastolic;
    }

    /**
     *
     * @param diastolic integer representing diastolic pressure
     */
    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    /**
     *
     * @param heartRate integer representing heartrate (BPM)
     */
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    /**
     *
     * @return heartrate in BPM
     */
    public int getHeartRate() {
        return heartRate;
    }

    /**
     *
     * @return string of comment
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment the comment to be set for the recording
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
