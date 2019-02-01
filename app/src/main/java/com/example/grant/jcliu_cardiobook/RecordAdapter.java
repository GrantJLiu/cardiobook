package com.example.grant.jcliu_cardiobook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordAdapter extends ArrayAdapter<Recording> {

    private Context context;
    private ArrayList<Recording> recordingList;

    public RecordAdapter(Context context, int resource, ArrayList<Recording> objects) {
        super(context, resource, objects);

        this.context = context;
        this.recordingList = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_record, null);
        }
        Recording element = recordingList.get(position);
        Log.d("Position List Size", Integer.toString(recordingList.size()));

        if (element != null){

            //LayoutInflater inflater = (LayoutInflater)
            //        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            //View view = inflater.inflate(R.layout.element_record, null);

            TextView title = (TextView) convertView.findViewById(R.id.record_list_item_title);
            TextView date = (TextView) convertView.findViewById(R.id.record_list_item_date);

            int sys = element.getSystolic();
            int dia = element.getDiastolic();

            String titleText = element.getSystolic() + "/" + element.getDiastolic() + " mmHg   ";
            title.setTextColor(Color.BLACK);
            if (sys < 90 || sys > 140 || dia < 60 || dia > 90){
                title.setTextColor(Color.RED);
            }
            titleText = titleText + element.getHeartRate() + " BPM";
            title.setText(titleText);
            date.setText(element.getDateFormatted(getContext()));
        }

        return convertView;
    }
}
