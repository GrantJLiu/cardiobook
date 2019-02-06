package com.example.grant.jcliu_cardiobook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activity that handles the editing and creation UI of a new or existing recording.
 * Takes user inputs defined on edit screen, packages it and passes the parcel to the parent activity
 */
public class RecordActivity extends AppCompatActivity {

    private EditText etSystolic;
    private EditText etDiastolic;
    private EditText etHeartRate;
    private EditText etComment;
    private EditText etDate;
    private EditText etTime;
    private Recording record;

    /**
     * On creation of the activity, we set the context view, get the intent, and check if it's
     * a previously existing recording. If it is we auto fill fields.
     * Date Time is auto filled regardless, but can be edited by user.
     * @param savedInstanceState Android bundle object passed on creation
     */
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
     * @return Something for android
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            // save case
            case R.id.action_recording_save:
                updateEditText();
                if (isValid()){ //validate input fields are filled
                    saveRecord();
                } else { // send user a message to fill in the required fields
                    Toast.makeText(this,"Please fill in fields", Toast.LENGTH_SHORT).show();
                }
                break;

            // deletion case
            case R.id.action_recording_delete:
                Intent retIntent = new Intent(); // intent to return to parent activity (main)
                Intent intent = getIntent(); // get intent sent from parent
                int i = intent.getIntExtra("Index",-1);
                if (i == -1) { // Can't delete an non-existing file
                    Toast.makeText(this,"nothing to delete", Toast.LENGTH_SHORT).show();
                    break;
                }

                // sends a message to confirm deletion, passes intent to parent with delete flags.
                Toast.makeText(this, "deleting recording", Toast.LENGTH_SHORT).show();
                retIntent.putExtra("delete", true);
                retIntent.putExtra("Index", i);
                setResult(Activity.RESULT_OK, retIntent);
                finish();
                break;
        }
        return true;
    }

    /**
     * Called when user presses the save button.
     * Exits the current activity, and passes the Recording Parcel to parent activity along
     * with some flags about the data.
     */
    private void saveRecord(){
        // extracting user text in boxes to machine data
        String temp = etSystolic.getText().toString();
        int systolic = Integer.parseInt(temp);
        temp = etDiastolic.getText().toString();
        int diastolic = Integer.parseInt(temp);
        temp = etHeartRate.getText().toString();
        int heartRate = Integer.parseInt(temp);
        String comment = etComment.getText().toString();

        // Special procedure needed to extract date time from user input
        String dateFormat = etDate.getText().toString() + " " + etTime.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm",Locale.CANADA);
        Date date = new Date();
        try {
            date = sdf.parse(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // making a new Recording with the values provided by the user.
        Recording record = new Recording(date, systolic, diastolic, heartRate, comment);
        Toast.makeText(this, "Recording saved!", Toast.LENGTH_SHORT).show();

        // Setting up the intent to pass back to parent, including the Recording parcel
        Intent recordIntent = new Intent();
        recordIntent.putExtra("Recording", record);

        // Special code used to see if it was a previously existing recording
        // passes up some information for existing recording
        Intent intent = getIntent();
        int i = intent.getIntExtra("Index",0);
        if (i != 0){
            recordIntent.putExtra("Index", i);
        }
        setResult(Activity.RESULT_OK, recordIntent);
        finish();
    }

    /**
     * Validates input by checking if required fields are filled by the user
     * Android xml should restrict user to correct type of input (numbers vs strings).
     * @return return true on valid, false otherwise
     */
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

    /**
     * updates the EditText we're getting from the user. Called multiple times within class.
     */
    private void updateEditText(){
        etSystolic =  ((EditText)findViewById(R.id.etSystolic));
        etDiastolic =  ((EditText)findViewById(R.id.etDiastolic));
        etHeartRate =  ((EditText)findViewById(R.id.etHeartRate));
        etComment =  ((EditText)findViewById(R.id.etComment));
        etTime = ((EditText)findViewById(R.id.etTime));
        etDate =  ((EditText)findViewById(R.id.etDate));
    }

    /**
     * Fills the text-boxes with any pre-existing data.
     */
    private void fillText(){
        updateEditText();
        etSystolic.setText(String.valueOf(record.getSystolic()));
        etDiastolic.setText(String.valueOf(record.getDiastolic()));
        etHeartRate.setText(String.valueOf(record.getHeartRate()));
        etComment.setText(record.getComment());
    }

    /**
     * Fills the date and time text box. Separate from fillText() because we want to only update
     * date time when creating a new recording
     * @param intent intent of the current activity. Used to extract information for date time.
     */
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
