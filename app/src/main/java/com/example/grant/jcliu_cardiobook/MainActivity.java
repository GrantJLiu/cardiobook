package com.example.grant.jcliu_cardiobook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/*
 * Made by Grant Liu, Last update 2/2/2019
 *
 */

/**
 * main activity class, responsible for displaying the list view on the main screen
 * also responsible for saving/loading from file, selecting list items, or adding a new recording
 *
 * Large parts of code, or general understanding of android came from vids below:
 * https://www.youtube.com/watch?v=ysEeCph0GPA
 * https://www.youtube.com/watch?v=4SLa3EucLI0&t=2055s
 */
public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "Recording.sav"; // save file name

    //ID of request codes to add/edit
    private static final int ADD_RECORD_REQUEST = 1;
    private static final int EDIT_RECORD_REQUEST = 2;

    //private ListView oldRecordings; // loaded from save file
    private ArrayList<Recording> recordList = new ArrayList<>(); //copied into memory
    private RecordAdapter adapter; // initialize adapter.


    /**
     * On create of the activity override
     * loads the file, sets up the adapter, loads file into arraylist, sets on click listener for
     * recordings in the listview
     * @param savedInstanceState Android bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView oldRecordings;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFromFile();
        adapter = new RecordAdapter(this, 0, recordList);
        oldRecordings = findViewById(R.id.mainRecordList);
        oldRecordings.setAdapter(adapter);

        // On click listener to find if a list item is tapped
        oldRecordings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * On click of an item, starts up an activity with result and passing some information
             * @param parent parent activity
             * @param view current view provided from android
             * @param position index of the item being clicked
             * @param id id of the item being clicked
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recording record = recordList.get(position);
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                intent.putExtra("Recording", record);
                intent.putExtra("Index", position);
                startActivityForResult(intent, EDIT_RECORD_REQUEST);

            }
        });


    }

    /**
     * Displays the little plus sign in top right of main menu to add new activity
     *
     * @param menu android menu item (activity_record)
     * @return always true so function doesn't complain
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * When the menu item (add new recording) is selected, do something
     *
     * @param item Save icon in top right corner
     * @return true so function doesn't complain
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_new_recording:
                //start RecordActivity as new activity
                startActivityForResult(new Intent(this, RecordActivity.class),
                        ADD_RECORD_REQUEST);
                break;
        }

        return true;
    }

    /**
     * saves the current list into a json file using Google Gson
     */
    private void saveInFile() {
        try {
            FileWriter out = new FileWriter(new File(getFilesDir(), FILENAME));
            Gson gson = new Gson();
            gson.toJson(recordList, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads the list stored in the save file using Google Gson
     */
    private void loadFromFile() {
        try {
            FileReader in = new FileReader(new File(getFilesDir(), FILENAME));
            Gson gson = new Gson();

            Type listtype = new TypeToken<ArrayList<Recording>>(){}.getType();
            recordList = gson.fromJson(in, listtype);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * On activity result, called after the sub activity (RecordActivity) ends.
     * Has 2 cases, one adds a new item to the list; the other handles editing/deleting of an
     * existing item.
     *
     * Regardless of case, calls saveInFile() and updates adapter display after.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_RECORD_REQUEST) { // adding a new record
            if (resultCode == Activity.RESULT_OK) {
                Recording record = data.getParcelableExtra("Recording");
                recordList.add(record);
            }
        } else if (requestCode == EDIT_RECORD_REQUEST) { // editing (and possible deletion)
            if (resultCode == Activity.RESULT_OK) {
                int index = data.getIntExtra("Index", 0);

                //if user tapped delete
                if (data.getBooleanExtra("delete", false)) {
                    recordList.remove(index);
                } else{ // not delete case
                    Recording record = data.getParcelableExtra("Recording");
                    recordList.set(index, record);
                }
            }
        }
        // update adapter, save to file
        adapter.notifyDataSetChanged();
        saveInFile();
    }
}
