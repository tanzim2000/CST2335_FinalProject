/*
 * @(#)DetailsFragment.java Mar 27, 2022
 * Professor: Frank Emanuel
 * CST82335-012 Project
 * Students: Xiaojie Zhao, Shanshu Hong, Jun Fan
 */
package com.cst2335.cst2335_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TicketSearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public final static String PREFERENCES_FILE = "citiesSearched";
    public static final String ACTIVITY_NAME = "MAIN_ACTIVITY";
    public final static String COSTUMER_KEY = "BIxLi1oQL6WVcqfYRvGlHzpUGhFkjE16";
    private static final String TAG = "MainActivity";
    MyOpener myOpenHelper;
    SQLiteDatabase eventDB;
    AsyncTask searchTask;
    private EditText cityEditText;
    private EditText radiusEditText;
    private SharedPreferences sharedPref;

    Intent goToEvent;
    String searchURL;
    EditText etCity;
    EditText searchR;
    ProgressBar pgbar;
    Button cancelBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_search);

        //add a toolbart
        Toolbar myToolbar = (Toolbar) findViewById(R.id.ticketToolbar);
        setSupportActionBar(myToolbar);

        etCity = findViewById(R.id.cityInput);
        searchR = findViewById(R.id.searchRadius);
        pgbar = (ProgressBar) findViewById(R.id.pgbar);
        cancelBt = findViewById(R.id.cancelBt);

        //create a preference file to save the latest input;
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        etCity.setText(prefs.getString(ACTIVITY_NAME, ""));

        //open the database save the search result
        myOpenHelper = new MyOpener(this);
        eventDB = myOpenHelper.getWritableDatabase();
        int version = MyOpener.VERSION;
        myOpenHelper.onUpgrade(eventDB, version, version + 1);


        //for getting last searched
        sharedPref = getSharedPreferences("Ticket", Context.MODE_PRIVATE);
        String citySaved = sharedPref.getString("city", "");
        String radiusSaved = sharedPref.getString("radius", "");

        cityEditText = findViewById(R.id.cityInput);
        radiusEditText = findViewById(R.id.searchRadius);

        // set saved city and radius
        cityEditText.setText(citySaved);
        radiusEditText.setText(radiusSaved);

        Button searchBt = findViewById(R.id.searchBt);
        searchBt.setOnClickListener((click) ->
        {
            //get user input to create search URL:
            String cityIn = etCity.getText().toString();
            String RadiusIn = searchR.getText().toString();

            saveSharedPrefs(cityIn, RadiusIn); //save last searched info.

            searchURL = "https://app.ticketmaster.com/discovery/v2/events.json?apikey="
                    + COSTUMER_KEY + "&city=" + cityIn + "&radius=" + RadiusIn;

            //create new AsyncTask to search the events
            MyHTTPRequest searchTask = new MyHTTPRequest();
            searchTask.execute(searchURL);  //AsyncTask Type 1

            //go to Profile page
            String searchHst = etCity.getText().toString();
            goToEvent = new Intent(TicketSearchActivity.this, EventList.class);

            //Save user input data to preference_file
            SharedPreferences.Editor writer = prefs.edit();
            writer.putString(PREFERENCES_FILE, searchHst);
            writer.apply(); //save to disk

            //Make the transition:
            //startActivity(goToEvent);

        });

        //Click cancel button to stop the search progress immediately
        cancelBt.setOnClickListener(view -> searchTask.cancel(true));


        //add a navigation view
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, myToolbar, R.string.help, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView;
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    //get favorite event list from database to show favorite list
        //open the database and create a query;
        MyOpener myOpenHelper = new MyOpener(this);
        eventDB = myOpenHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = eventDB.rawQuery("select * from " +
                MyOpener.FAVORITE_TABLE_NAME + ";", null);

        //Convert column names to indices:
        int eventDBId = cursor.getColumnIndex(MyOpener.COL_ID);
        int eventDBName = cursor.getColumnIndex(MyOpener.COL_EventName);
        int eventDBDate = cursor.getColumnIndex(MyOpener.COL_StartDate);
        int eventDBMinP = cursor.getColumnIndex(MyOpener.COL_MIN_Price);
        int eventDBMaxP = cursor.getColumnIndex(MyOpener.COL_MAX_Price);
        int eventDBURL = cursor.getColumnIndex(MyOpener.COL_URL);
        int imgDBURL = cursor.getColumnIndex(MyOpener.COL_IMG);

        //add elements to Arraylist of events
        while (cursor.moveToNext()){
            int id = cursor.getInt(eventDBId);
            String eventName = cursor.getString(eventDBName);
            String eventDate = cursor.getString(eventDBDate);
            double eventMinP = cursor.getDouble(eventDBMinP);
            double eventMaxP = cursor.getDouble(eventDBMaxP);
            String eventURL = cursor.getString(eventDBURL);
            String imgURL = cursor.getString(imgDBURL);

            ArrayList<Events> favoriteList = new ArrayList<>();
            favoriteList.add(new Events(id,eventName,eventDate,eventMinP, eventMaxP, eventURL, imgURL));
        }


    }

    //inflat toolbart
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_menu, menu);

        return true;
    }


    //for saving the last searched information
    private void saveSharedPrefs(String city, String radius) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("city", city);
        editor.putString("radius", radius);
        editor.commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item0:

                Intent goToHome = new Intent(TicketSearchActivity.this, MainActivity.class);
                startActivity(goToHome);
                break;

            case R.id.item1:
                Intent goToTicket = new Intent(TicketSearchActivity.this, TicketSearchActivity.class);
                startActivity(goToTicket);
                break;

            case R.id.item2:
                String title = getResources().getString(R.string.help);

                String line2 = getResources().getString(R.string.help);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(title)

                        .setMessage(line2 + " \n"

                        )
                        .setNegativeButton("Close", (click, arg) -> {
                        })

                        .create().show();
                break;
        }
        return true;

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String message = null;

        switch (item.getItemId()) {
            case R.id.item0:

                Intent goToHome = new Intent(TicketSearchActivity.this, MainActivity.class);
                startActivity(goToHome);
                break;

            case R.id.item1:
                Intent goToTicket = new Intent(TicketSearchActivity.this, TicketSearchActivity.class);
                startActivity(goToTicket);
                break;

            case R.id.item2:
                String title = getResources().getString(R.string.help);
                String line2 = getResources().getString(R.string.help);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(title)

                        .setMessage(line2 + " \n"

                        )
                        .setNegativeButton("Close", (click, arg) -> {
                        })


                        .create().show();

                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.main_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * create MyHTTPRequest class to get information from https://www.ticketmaster.ca/
     *
     */
    @SuppressLint("StaticFieldLeak")
    private class MyHTTPRequest extends AsyncTask<String, ProgressBar, String> {
        //static private final String TAG = "MyHTTPRequest";
        @Override
        public String doInBackground(String... args) {
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
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString(); //result is the whole string

                // convert string to JSON: Look at slide 27:
                JSONObject webResult = new JSONObject(result);
                JSONArray jSONEventArray = webResult.getJSONObject("_embedded")
                        .getJSONArray("events");
                // JSONObject anEvent = jSONEventArray.getJSONObject(0);

                for (int i = 0; i < jSONEventArray.length(); i++) {
                    JSONObject anEvent = jSONEventArray.getJSONObject(i);

                    // Pulling items from the array to SQLite
                    String eventName = anEvent.getString("name");
                    String eventURL = anEvent.getString("url");
                    String eventDate = anEvent.getJSONObject("dates").
                            getJSONObject("start").getString("localDate");
                    double eventMinP = anEvent.getJSONArray("priceRanges").
                            getJSONObject(0).getDouble("min");
                    double eventMaxP = anEvent.getJSONArray("priceRanges").
                            getJSONObject(0).getDouble("max");
                    String imgURL = anEvent.getJSONArray("images")
                            .getJSONObject(0).getString("url");

                    ContentValues newRow = new ContentValues();
                    newRow.put(MyOpener.COL_EventName, eventName);
                    newRow.put(MyOpener.COL_URL, eventURL);
                    newRow.put(MyOpener.COL_StartDate, eventDate);
                    newRow.put(MyOpener.COL_MIN_Price, eventMinP);
                    newRow.put(MyOpener.COL_MAX_Price, eventMaxP);
                    newRow.put(MyOpener.COL_IMG, downloadImage(imgURL,anEvent.getString("id")));
                    long id = eventDB.insert(MyOpener.TABLE_NAME, null, newRow);
                }

                //  publishProgress(25);
                //Thread.sleep(1000);
                // publishProgress(50);

            } catch (Exception ignored) {

            }

            return "Done";
        }

        private String downloadImage(String imageURL,String id) throws IOException {

            URL url = new URL(imageURL);

            //open new connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // get image from url
            Bitmap image = BitmapFactory.decodeStream(urlConnection.getInputStream());
            String imageFile = id + ".png";
            if(fileExists(imageFile)){
                return imageFile;
            }else {
                FileOutputStream outputStream = openFileOutput(imageFile, Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 100,outputStream);
                outputStream.flush();
                outputStream.close();
                return imageFile;
            }
        }

        public boolean fileExists(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        //show the progress percentage when searching
        public void onProgressUpdate(Integer... args) {
            Log.i(TAG, "In onProgressUpdate");
            pgbar.setVisibility(View.VISIBLE);
        }

        //Type3
        //for making the progress bar visible, added the "INVISIBLE"part
        public void onPostExecute(String fromDoInBackground) {
            //Make the transition:
            startActivity(goToEvent);
            Log.i(TAG, fromDoInBackground);
            pgbar.setVisibility(View.INVISIBLE);
//        super.onPostExecute(fromDoInBackground);
        }

        //for making the progress bar visible, added the "VISIBLE"part
        //source: https://stackoverflow.com/questions/19005014/visibility-of-progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pgbar.setVisibility(View.VISIBLE);
        }

    }

}