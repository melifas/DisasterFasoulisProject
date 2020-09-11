package com.example.disasterfasoulisproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.disasterfasoulisproject.Model.Earthquake;
import com.example.disasterfasoulisproject.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Earthquakes extends AppCompatActivity implements SensorEventListener {

    private static final int REQ_CODE = 10;

     //Earthquake earthquake = new Earthquake();
    ArrayList<Earthquake> earthquakes;

    private RequestQueue queue;
    LocationManager locationManager;
    Location loc;
    double latitude;
    double longtitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquakes);

        earthquakes = new ArrayList<>();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        queue = Volley.newRequestQueue(this);
    }

   //----------------------------------------- nees meuodoi----------------------------------------//
   public void parseJsonRequest(String response) {


       if (response != null) {
           // Catch the exception so the app doesn't crash, and print the error message to the logs.
           try {
               JSONObject reader = new JSONObject(response);
               JSONArray features = reader.getJSONArray("features");
               for (int index = 0; index < features.length(); index++) {
                   JSONObject properties = reader.getJSONArray("features").getJSONObject(index).getJSONObject("properties");
                   Double magnitude = properties.getDouble("mag");
                   String place = properties.getString("place");
                   String url = properties.getString("url");
                   //This split between offset location and primary location
                   String[] separated = place.split(",");
                   //If there is missing field jump to the next item
                   if (separated.length < 2) {
                       continue;
                   }
                   //This contain the offsetLocation
                   String offsetLocation = separated[0];
                   //This contain the primaryLocation
                   separated[1] = separated[1].trim();
                   String primaryLocation = separated[1];
                   //Passing date twice one for the date and one for the time.
                   String updateDate = properties.getString("time");
                   java.util.Date date = new java.util.Date(Long.valueOf(updateDate));
                   //The second date passing the time
                   //earthquakes.add(new Earthquake(magnitude, offsetLocation, primaryLocation, date, date, url));

               }


           } catch (JSONException e) {
               // If an error is thrown when executing any of the above statements in the "try" block,
               // catch the exception here, so the app doesn't crash. Print a log message
               // with the message from the exception.
               Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
           }
       }
   }

   //---------------------------------------------nees------------------------------------------//
   public void createJsonRequest() {

// Instantiate the cache
       Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
       Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
       RequestQueue mRequestQueue = new RequestQueue(cache, network);

// Start the queue
       mRequestQueue.start();

       String url = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_month.geojson";

// Formulate the request and handle the response.
       StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       //Passing the response
                       parseJsonRequest(response);

                       /*adapter = new EarthQuakeAdapter(MainActivity.this, earthquakes);
                       listView.setAdapter(adapter);*/
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       // Handle error
                   }
               });

// Add the request to the RequestQueue.
       mRequestQueue.add(stringRequest);

   }
//-----------------------------------------------------------------------------------------------//
    /*private ArrayList<Earthquake> getEarthquakes() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Earthquake earthquake = new Earthquake();

                            JSONArray features = response.getJSONArray("features");

                            for (int i = 0; i < Constants.LIMIT ; i++){
                                JSONObject properties = features.getJSONObject(i).getJSONObject("properties");

                                JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");

                                JSONArray coordinates  = geometry.getJSONArray("coordinates");

                                double lot = coordinates.getDouble(0);
                                double lat = coordinates.getDouble(1);

                                Log.d("Quake", lot + " " + lat);


                                earthquake.setPlace(properties.getString("place"));
                                earthquake.setType(properties.getString("type"));
                                earthquake.setTime(properties.getLong("time"));
                                earthquake.setMagnitude(properties.getDouble("mag"));
                                earthquake.setDetailLink(properties.getString("detail"));

                                //earthquakes.add(earthquake);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
       // earthquakes.add(earthquake);
        return earthquakes;
    }*/
//-------------------------------------------------------------------------------------------------------------------//
    @Override
    public void onSensorChanged(SensorEvent sensorEvent ) {
        Sensor mySensor = sensorEvent.sensor;

        createJsonRequest();

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER){


            float[] values = sensorEvent.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            double rootSquare = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
            if (rootSquare<2.0){
                Toast.makeText(this, "Fall Detected", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}




