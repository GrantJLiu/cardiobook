package com.example.grant.jcliu_cardiobook;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Made by Grant Liu, 30/01/2019
 */

// Parcelable object code/learning:
// https://www.sitepoint.com/transfer-data-between-activities-with-android-parcelable/
public class Recording implements Parcelable {
    private Date date;
    private int systolic;
    private int diastolic;
    private int heartRate;
    private String comment;

    /**
     * writes the current state of the Recording to a parcel for use in other activities
     * @param out parcel object to be outputted for useage
     * @param flag flags (0/1) for Parcelable
     */
    public void writeToParcel(Parcel out, int flag) {
        out.writeLong(date.getTime());
        out.writeInt(systolic);
        out.writeInt(diastolic);
        out.writeInt(heartRate);
        out.writeString(comment);
    }

    public Recording(Date date, int sys, int dia, int heartR, String comment) {
        this.date = date;
        this.systolic = sys;
        this.diastolic = dia;
        this.heartRate = heartR;
        this.comment = comment;
    }

    public Recording(Parcel parcel){
        date = new Date(parcel.readLong());
        systolic = parcel.readInt();
        diastolic = parcel.readInt();
        heartRate = parcel.readInt();
        comment = parcel.readString();
    }

    public static final Parcelable.Creator<Recording> CREATOR
            = new Parcelable.Creator<Recording>() {
        @Override
        public Recording createFromParcel(Parcel parcel) {
            return new Recording(parcel);
        }

        @Override
        public Recording[] newArray(int size) {
            return new Recording[0];
        }
    };

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
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

    // required for parcelable
    public int describeContents() {
        return hashCode();
    }
}
