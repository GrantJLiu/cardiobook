package com.example.grant.jcliu_cardiobook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "Recording.sav";
    private static final int ADD_RECORD_REQUEST = 1;
    private static final int EDIT_RECORD_REQUEST = 2;

    private ListView oldRecordings; // loaded from save file
    private ArrayList<Recording> recordList = new ArrayList<>(); //copied into memory
    private RecordAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFromFile();
        adapter = new RecordAdapter(this, 0, recordList);
        oldRecordings = (ListView) findViewById(R.id.mainRecordList);
        oldRecordings.setAdapter(adapter);
        oldRecordings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
     * @param item selected item
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

    private void loadFromFile() {
        try {
            FileReader in = new FileReader(new File(getFilesDir(), FILENAME));
            Gson gson = new Gson();

            Type listtype = new TypeToken<ArrayList<Recording>>(){}.getType();
            recordList = gson.fromJson(in, listtype);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_RECORD_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Recording record = data.getParcelableExtra("Recording");
                recordList.add(record);
            }
        } else if (requestCode == EDIT_RECORD_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                int index = data.getIntExtra("Index", 0);
                if (data.getBooleanExtra("delete", false)) {
                    recordList.remove(index);
                } else{
                    Recording record = data.getParcelableExtra("Recording");
                    recordList.set(index, record);
                }
            }
        }
        adapter.notifyDataSetChanged();
        saveInFile();
    }
}
