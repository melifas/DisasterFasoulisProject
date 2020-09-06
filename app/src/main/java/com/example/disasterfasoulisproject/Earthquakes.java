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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
        
         int count = getEarthquakes().size();

         Log.d("count", String.valueOf(count));
    }

    private ArrayList<Earthquake> getEarthquakes() {

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
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent ) {
        Sensor mySensor = sensorEvent.sensor;


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




