package com.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AppCompatActivity;

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
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public final static String PREFERENCES_FILE = "citiesSearched";
    public static final String ACTIVITY_NAME = "MAIN_ACTIVITY";
    public final static String COSTUMER_KEY = "BIxLi1oQL6WVcqfYRvGlHzpUGhFkjE16";
    private static final String TAG = "MainActivity";
    MyOpener myOpenHelper;
    SQLiteDatabase eventDB;
    boolean isTablet;
    public ArrayList<Events> eventList = new ArrayList<>(  );
    AsyncTask searchTask;
    //MyListAdapter myAdapter;
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

        SharedPreferences prefs = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        etCity.setText(prefs.getString(ACTIVITY_NAME, ""));

        Button searchBt = findViewById(R.id.searchBt);
        searchBt.setOnClickListener((click) ->
        {

            String cityIn = etCity.getText().toString();
            String RadiusIn = searchR.getText().toString();
            searchURL =  "https://app.ticketmaster.com/discovery/v2/events.json?apikey="
                    + COSTUMER_KEY+"&city="+cityIn+"&radius="+RadiusIn ;
            //create new AsyncTask to search the events
            MyHTTPRequest searchTask = new MyHTTPRequest();
            searchTask.execute(searchURL);  //AsyncTask Type 1

            //go to Profile page
            String searchHst = etCity.getText().toString();
            Intent goToEvent = new Intent(MainActivity.this ,EventList.class);
           // goToEvent.putExtra(PREFERENCES_FILE, searchHst);

            //Save user input data to preference_file
            SharedPreferences.Editor writer = prefs.edit();
            writer.putString(PREFERENCES_FILE, searchHst);
            writer.apply(); //save to disk

            //Make the transition:
            //startActivity(goToEvent);


        });

        //Click cancel button to stop the search progress immediately
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTask.cancel(true);
            }});
    }

    //create MyHTTPRequest class to get information from https://www.ticketmaster.ca/
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

                // Starts the search
                urlConnection.connect();

            //wait for data:
            InputStream response = urlConnection.getInputStream();

            //Create a Pull parser uses the Factory pattern
          /*XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( response  , "UTF-8");*/

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
            JSONObject webResult = new JSONObject(result);
                JSONArray eventArray = webResult.getJSONObject("_embedded").getJSONArray("events");
            /*
                String nameArray[]=null;

                for (int i=0; i < eventArray.length(); i++)
                    try {
                        JSONObject anEvent = eventArray.getJSONObject(i);

                         // Pulling items from the array
                        String eventName= anEvent.getString("name");
                        nameArray[i]=eventName;
                        //anEvent.getString("url");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } */

            //get the double associated with "value"
            //int numEntries = eventResult.getInt("count");

          //  publishProgress(25);
            //Thread.sleep(1000);
           // publishProgress(50);
            Log.i(TAG, "Num of entries: " +eventArray) ;

        }
        catch (Exception e)
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
        Log.i(TAG, fromDoInBackground);
    }

        public void execute(int i) {
        }
    }

    //It creates an instance of the List View
    // private class MyListAdapter extends BaseAdapter { }

    //create Events class to set up Arraylist
    public class Events{

        long id;
        String eventName;
        Date startDate;
        double minPrice;
        double maxPrice;
        String ticketMasterURL;

        public void Evnets(long id, String eventName, Date startDate,
                           double minPrice, double maxPrice, String ticketMasterURL)
        {
            this.id=id;
            this.eventName=eventName;
            this.startDate=startDate;
            this.minPrice=minPrice;
            this.maxPrice=maxPrice;
            this.ticketMasterURL=ticketMasterURL;

        }

        public long getId(){return id;}
        public String getEventName(){return eventName;}
        public Date getStartDate(){return startDate;}
        public double getMinPrice(){return minPrice;}
        public double getMaxPrice(){return maxPrice;}
        public String getTicketMasterURL(){return ticketMasterURL;}
    }
}