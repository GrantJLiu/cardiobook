package com.example.grant.jcliu_cardiobook;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom adapter for the listview of recordings
 * Handles the view of a singular item in the list, including changing the colour of text depending
 * on the pressure.
 *
 * Adapter customization learned from following site:
 * https://www.sitepoint.com/custom-data-layouts-with-your-own-android-arrayadapter/
 */
public class RecordAdapter extends ArrayAdapter<Recording> {

    private ArrayList<Recording> recordingList;

    /**
     * standard constructor from parent
     * @param context Android context
     * @param resource Android resource (used in super only)
     * @param objects ArrayList of our objects
     */
    public RecordAdapter(Context context, int resource, ArrayList<Recording> objects) {
        super(context, resource, objects);

        this.recordingList = objects;
    }

    /**
     * Method used to create the view for each individual item
     * @param position index of the item in the list
     * @param convertView view object for method to convert, is later returned
     * @param parent parent activity
     * @return A view object used by Android to display our individual item in list view
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){ // check if given view is null, if it is we inflate
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_record, null);
        }
        Recording element = recordingList.get(position); // extract our recording from the list

        if (element != null){

            // Two textview objects we later display
            TextView title = (TextView) convertView.findViewById(R.id.record_list_item_title);
            TextView date = (TextView) convertView.findViewById(R.id.record_list_item_date);

            // Extract some information
            int sys = element.getSystolic();
            int dia = element.getDiastolic();

            // Colour it red if abnormal sys/dia pressure, otherwise colour it black
            String titleText = element.getSystolic() + "/" + element.getDiastolic() + " mmHg   ";
            title.setTextColor(Color.BLACK); //needed to set text back to black after an edit
            if (sys < 90 || sys > 140 || dia < 60 || dia > 90){
                title.setTextColor(Color.RED);
            }
            titleText = titleText + element.getHeartRate() + " BPM"; // add heart rate
            title.setText(titleText); //set the text here
            date.setText(element.getDateFormatted(getContext())); // set text to formatted date
        }

        return convertView;
    }
}
