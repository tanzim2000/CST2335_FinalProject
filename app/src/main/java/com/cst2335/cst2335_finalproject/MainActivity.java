package com.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ArrayList<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("onCreate()", String.valueOf(events.size()));

        EditText cityInput = findViewById(R.id.city);
        EditText radiusInput = findViewById(R.id.radius);
        Button searchBtn = findViewById(R.id.search);
        progressBar = findViewById(R.id.progress);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityInput.getText().toString();
                String radius = radiusInput.getText().toString();
                MyHTTPRequest req = new MyHTTPRequest();
                req.execute("https://app.ticketmaster.com/discovery/v2/events.json?apikey=SitXAJg10UsCGeKcSzQ3lB5aJb272G7t&city="+city+"&radius="+radius);
            }

        });


    }

    private class MyHTTPRequest extends AsyncTask< String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            publishProgress(20);
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                Log.e("doInBackground",result);
                JSONObject jsonObject = new JSONObject(result);
                JSONObject embedded = jsonObject.getJSONObject("_embedded");
                JSONArray jArray = embedded.getJSONArray("events");
                for (int i=0; i < jArray.length(); i++){
                    JSONObject eventObject = jArray.getJSONObject(i);

                     String eventName = eventObject.getString("name");
                     String startDate = eventObject.getJSONObject("dates").getJSONObject("start").getString("dateTime");
                     double minPrice = eventObject.getJSONArray("priceRanges").getJSONObject(0).getDouble("min");
                     double maxPrice = eventObject.getJSONArray("priceRanges").getJSONObject(0).getDouble("max");
                     String ticketMasterUrl = eventObject.getString("url");
                     String imagePath = null;

                    Event event = new Event(eventName,startDate,minPrice, maxPrice,ticketMasterUrl,imagePath);
                    events.add(event);
                }
                publishProgress(100);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            Log.e("onPostExecute()", String.valueOf(events.size()));
        }
    }

}