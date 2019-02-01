package com.example.grant.jcliu_cardiobook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RecordActivity extends AppCompatActivity {

    private EditText etSystolic;
    private EditText etDiastolic;
    private EditText etHeartRate;
    private EditText etComment;
    private EditText etDate;
    private EditText etTime;
    private Recording record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Intent intent = getIntent();
        if (intent.getParcelableExtra("Recording") != null){
            this.record = intent.getParcelableExtra("Recording");
            fillText();
        }
        fillDateTime(intent);
    }

    /**
     * Displays the save icon in recording activity
     *
     * @param menu menu item of save icon
     * @return true for android api
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recording_newrecord, menu);
        return true;
    }


    /**
     * Method used when a user taps a menu icon
     * Tapping Save saves the new or updated recording
     * tapping delete deletes the recording from the file.
     * @param item a menu item the user clicked
     * @return boolean for something?
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_recording_save:
                updateEditText();

                //TODO verify inputs
                if (isValid()){
                    saveRecord();
                } else {
                    Toast.makeText(this,"Please fill in fields", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_recording_delete:
                //TODO delete recording;
                Intent retIntent = new Intent();
                Intent intent = getIntent();
                int i = intent.getIntExtra("Index",0);
                Toast.makeText(this, "deleting recording", Toast.LENGTH_SHORT).show();
                retIntent.putExtra("delete", true);
                retIntent.putExtra("Index", i);
                setResult(Activity.RESULT_OK, retIntent);
                finish();
                break;
        }
        return true;
    }

    private void saveRecord(){
        String temp = etSystolic.getText().toString();
        int systolic = Integer.parseInt(temp);
        temp = etDiastolic.getText().toString();
        int diastolic = Integer.parseInt(temp);
        temp = etHeartRate.getText().toString();
        int heartRate = Integer.parseInt(temp);
        String comment = etComment.getText().toString();

        String dateFormat = etDate.getText().toString() + " " + etTime.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm",Locale.CANADA);
        Date date = new Date();
                try {
                    date = sdf.parse(dateFormat);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

        Recording record = new Recording(date, systolic, diastolic, heartRate, comment);
        Toast.makeText(this, "Recording saved!", Toast.LENGTH_SHORT).show();


        Intent recordIntent = new Intent();
        recordIntent.putExtra("Recording", record);

        Intent intent = getIntent();
        int i = intent.getIntExtra("Index",0);
        if (i != 0){
            recordIntent.putExtra("Index", i);
        }
        setResult(Activity.RESULT_OK, recordIntent);
        finish();
    }

    private boolean isValid(){
        if (TextUtils.isEmpty(etSystolic.getText().toString())){
            return false;
        } else if (TextUtils.isEmpty(etDiastolic.getText().toString())){
            return false;
        } else if (TextUtils.isEmpty(etHeartRate.getText().toString())) {
            return false;
        }
        return true;
    }

    private void updateEditText(){
        etSystolic =  ((EditText)findViewById(R.id.etSystolic));
        etDiastolic =  ((EditText)findViewById(R.id.etDiastolic));
        etHeartRate =  ((EditText)findViewById(R.id.etHeartRate));
        etComment =  ((EditText)findViewById(R.id.etComment));
        etTime = ((EditText)findViewById(R.id.etTime));
        etDate =  ((EditText)findViewById(R.id.etDate));
    }

    private void fillText(){
        updateEditText();
        etSystolic.setText(String.valueOf(record.getSystolic()));
        etDiastolic.setText(String.valueOf(record.getDiastolic()));
        etHeartRate.setText(String.valueOf(record.getHeartRate()));
        etComment.setText(record.getComment());
    }

    private void fillDateTime(Intent intent){
        updateEditText();
        Date date;
        if (intent.getParcelableExtra("Recording") != null){
            date = record.getDate();
        } else {
            Calendar cal = Calendar.getInstance();
            date = cal.getTime();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.CANADA);
        etDate.setText(dateFormat.format(date));
        etTime.setText(timeFormat.format(date));
    }

}
