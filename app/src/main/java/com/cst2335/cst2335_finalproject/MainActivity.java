package com.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public final static String PREFERENCES_FILE = "citiesSearched";
    public static final String ACTIVITY_NAME = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etCity =findViewById(R.id.cityInput);
        String cityIn = etCity.getText().toString();

        MyHTTPRequest req = new MyHTTPRequest();
        String url =  "https://app.ticketmaster.com/discovery/v2/events.json?apikey="
                +"BIxLi1oQL6WVcqfYRvGlHzpUGhFkjE16&city="
                +cityIn+"&radius=100";
        req.execute(url);  //Type 1

        SharedPreferences prefs = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        etCity.setText(prefs.getString(ACTIVITY_NAME, ""));

        Button searchBt = findViewById(R.id.searchBt);
        searchBt.setOnClickListener((click) ->
        {
            //go to Profile page
            String searchHst = etCity.getText().toString();
            Intent goToEvent = new Intent(MainActivity.this ,EventList.class);
            goToEvent.putExtra(PREFERENCES_FILE, searchHst);

            //Save user input data to preference_file
            SharedPreferences.Editor writer = prefs.edit();
            writer.putString(PREFERENCES_FILE, searchHst);
            writer.apply(); //save to disk

            //Make the transition:
            startActivity(goToEvent);
        });
    }

    //Type1     Type2   Type3
    private class MyHTTPRequest extends AsyncTask< String, Integer, String>
    {
        static private final String TAG = "MyHTTPRequest";

    //Type3                Type1
    public String doInBackground(String ... args)
    {
        try {
            //create a URL object of what server to contact:
            URL url = new URL(args[0]);

            //open the connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //wait for data:
            InputStream response = urlConnection.getInputStream();

            //Create a Pull parser uses the Factory pattern
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( response  , "UTF-8");

            //JSON reading:
            //Build the entire string response:
            BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            String result = sb.toString(); //result is the whole string


            // convert string to JSON: Look at slide 27:
            JSONObject uvReport = new JSONObject(result);

            //get the double associated with "value"
            int numEntries = uvReport.getInt("count");

            publishProgress(25);
            Thread.sleep(1000);
            publishProgress(50);
            Log.i(TAG, "Num of entries: " + numEntries) ;

        }
        catch (Exception e)
        {

        }

        return "Done";
    }

    //Type 2
    public void onProgressUpdate(Integer ... args)
    {
        Log.i(TAG, "onProgressUpdate");
    }
    //Type3
    public void onPostExecute(String fromDoInBackground)
    {
        Log.i(TAG, fromDoInBackground);
    }
    }
}