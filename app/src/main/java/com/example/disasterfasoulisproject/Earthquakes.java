package com.example.disasterfasoulisproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Map;

public class Earthquakes extends AppCompatActivity implements SensorEventListener {

    private static final int REQ_CODE = 10;

    Button btnTestQuakes;

    private SensorManager sensorManager;

     Earthquake earthquake = new Earthquake();
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


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        btnTestQuakes = findViewById(R.id.btnTestEarthquakes);

        earthquakes = new ArrayList<>();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        queue = Volley.newRequestQueue(this);

        getEarthquakes();

       // createJsonRequest();
         //int count = earthquakes.size();

        /* btnTestQuakes.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getEarthquakes();
             }
         });*/


    }

   //----------------------------------------- nees meuodoi----------------------------------------//
   public void parseJsonRequest(JSONObject response) {

       if (response != null) {
           try {

               //Earthquake earthquake = new Earthquake();
               JSONObject reader = new JSONObject((Map) response);
               JSONArray features = reader.getJSONArray("features");

               for (int i = 0; i < Constants.LIMIT ; i++){
                   JSONObject properties = features.getJSONObject(i).getJSONObject("properties");

                   JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");

                   JSONArray coordinates  = geometry.getJSONArray("coordinates");

                   double lot = coordinates.getDouble(0);
                   double lat = coordinates.getDouble(1);

                   Log.d("Quake", lot + " " + lat);


                  /* earthquake.setPlace(properties.getString("place"));
                   earthquake.setType(properties.getString("type"));
                   earthquake.setTime(properties.getLong("time"));
                   earthquake.setMagnitude(properties.getDouble("mag"));
                   earthquake.setDetailLink(properties.getString("detail"));*/

                   earthquakes.add(new Earthquake(properties.getDouble("mag"),properties.getLong("time"),properties.getString("detail"),
                           properties.getString("type"),lot,lat));


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

       String url = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.geojson";


       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
               (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                   @Override
                   public void onResponse(JSONObject response) {
                    //getEarthquakes(response);
                   }
               }, new Response.ErrorListener() {

                   @Override
                   public void onErrorResponse(VolleyError error) {
                       // TODO: Handle error

                   }
               });

// Add the request to the RequestQueue.
       mRequestQueue.add(jsonObjectRequest);

   }
//-----------------------------------------------------------------------------------------------//
    private void getEarthquakes() {

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

                                    earthquakes.add(new Earthquake(properties.getDouble("mag"),properties.getLong("time"),properties.getString("detail"),
                                            properties.getString("type"),lot,lat));


                                    int count = earthquakes.size();
                                    Log.d("melifas",String.valueOf(count));

                                   /* for ( Earthquake earthquake1: earthquakes) {
                                        Log.d("details",earthquake.getPlace());
                                    }*/
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
        }

//-------------------------------------------------------------------------------------------------------------------//
    @Override
    public void onSensorChanged(SensorEvent sensorEvent ) {
        Sensor mySensor = sensorEvent.sensor;

        getEarthquakes();
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER){


            float[] values = sensorEvent.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            Log.d("deatilsforme",String.valueOf(earthquake.getLat()));

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




