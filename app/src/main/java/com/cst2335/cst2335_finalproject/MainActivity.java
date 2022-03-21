package com.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public final static String PREFERENCES_FILE = "citiesSearched";
    public static final String ACTIVITY_NAME = "MAIN_ACTIVITY";
    public final static String COSTUMER_KEY = "BIxLi1oQL6WVcqfYRvGlHzpUGhFkjE16";
    private static final String TAG = "MainActivity";
    MyOpener myOpenHelper;
    SQLiteDatabase eventDB;
    AsyncTask searchTask;

    Intent goToEvent;
    String searchURL;
    EditText etCity;
    EditText searchR;
    ProgressBar pgbar;
    Button cancelBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         etCity =findViewById(R.id.cityInput);
         searchR =findViewById(R.id.searchRadius);
         pgbar = findViewById(R.id.pgbar);
         cancelBt = findViewById( R.id.cancelBt);

         //create a preference file to save the latest input;
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        etCity.setText(prefs.getString(ACTIVITY_NAME, ""));

        //open the database save the search result
        myOpenHelper = new MyOpener(this);
        eventDB = myOpenHelper.getWritableDatabase();
        int version = MyOpener.VERSION;
        myOpenHelper.onUpgrade(eventDB,version,version+1);

        Button searchBt = findViewById(R.id.searchBt);
        searchBt.setOnClickListener((click) ->
        {
            //get user input to create search URL:
            String cityIn = etCity.getText().toString();
            String RadiusIn = searchR.getText().toString();
            searchURL =  "https://app.ticketmaster.com/discovery/v2/events.json?apikey="
                    + COSTUMER_KEY+"&city="+cityIn+"&radius="+RadiusIn ;

            //create new AsyncTask to search the events
            MyHTTPRequest searchTask = new MyHTTPRequest();
            searchTask.execute(searchURL);  //AsyncTask Type 1

            //go to Profile page
            String searchHst = etCity.getText().toString();
            goToEvent = new Intent(MainActivity.this ,EventList.class);

            //Save user input data to preference_file
            SharedPreferences.Editor writer = prefs.edit();
            writer.putString(PREFERENCES_FILE, searchHst);
            writer.apply(); //save to disk

            //Make the transition:
            //startActivity(goToEvent);

        });

        //Click cancel button to stop the search progress immediately
        cancelBt.setOnClickListener(view -> searchTask.cancel(true));
    }

    //create MyHTTPRequest class to get information from https://www.ticketmaster.ca/
    @SuppressLint("StaticFieldLeak")
    private class MyHTTPRequest extends AsyncTask<String, ProgressBar, String>
    {
        //static private final String TAG = "MyHTTPRequest";
        @Override
        public String doInBackground(String ... args)
        {
            try {
            //create a URL object of what server to contact:
            //URL url = new URL(searchURL);
                URL url = new URL(args[0]);

            //open the connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //wait for data:
            InputStream response = urlConnection.getInputStream();


            //JSON reading:
            //Build the entire string response:
            BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line).append("\n");
            }
            String result = sb.toString(); //result is the whole string

            // convert string to JSON: Look at slide 27:
            JSONObject webResult = new JSONObject(result);
            JSONArray jSONEventArray = webResult.getJSONObject("_embedded")
                    .getJSONArray("events");
               // JSONObject anEvent = jSONEventArray.getJSONObject(0);

                for (int i=0; i < jSONEventArray.length(); i++){
                   JSONObject anEvent = jSONEventArray.getJSONObject(i);

                    // Pulling items from the array to SQLite
                    String eventName= anEvent.getString("name");
                    String eventURL= anEvent.getString("url");

                    ContentValues newRow = new ContentValues();
                    newRow.put(MyOpener.COL_EventName,eventName);
                    newRow.put(MyOpener.COL_URL,eventURL);
                    long id = eventDB.insert(MyOpener.TABLE_NAME,null,newRow);
                    }

          //  publishProgress(25);
            //Thread.sleep(1000);
           // publishProgress(50);

            }
        catch (Exception ignored)
        {

        }

        return "Done";
    }

    //show the progress percentage when searching
    public void onProgressUpdate(Integer ... args)
    {
        Log.i(TAG, "In onProgressUpdate");
        //pgbar.setVisibility(View.VISIBLE);
    }
    //Type3
    public void onPostExecute(String fromDoInBackground)
    {
        //Make the transition:
       startActivity(goToEvent);
       Log.i(TAG, fromDoInBackground);
    }

    }

}