package com.example.grant.jcliu_cardiobook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "Recording.sav";

    private ListView oldRecordings; // loaded from save file
    private ArrayList<Recording> recordList = new ArrayList<Recording>(); //copied into memory


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        oldRecordings = (ListView) findViewById(R.id.mainRecordList);


    }

    /**
     * Displays the little plus sign in top right of main menu to add new activity
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
     * @param item selected item
     * @return true so function doesn't complain
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_main_new_recording:
                //start RecordActivity as new activity
                startActivity(new Intent(this, RecordActivity.class));
                saveInFile();
                break;
        }

        return true;
    }

    private void saveInFile(){
        try{
            FileWriter out = new FileWriter(new File(getFilesDir(), FILENAME));
            Gson gson = new Gson();
            gson.toJson(recordList, out);
            out.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
