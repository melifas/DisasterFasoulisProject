package com.example.disasterfasoulisproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.disasterfasoulisproject.Model.Earthquake;
import com.example.disasterfasoulisproject.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Earthquakes extends AppCompatActivity {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquakes);
        
        getEarthquakes();
    }

    private void getEarthquakes() {

        final Earthquake earthquake = new Earthquake();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray features = response.getJSONArray("features");

                            for (int i = 0; i < Constants.LIMIT ; i++){
                                JSONObject properties = features.getJSONObject(i).getJSONObject("properties");

                                JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");

                                JSONArray coordinates  = geometry.getJSONArray("coordinates");

                                double lot = coordinates.getDouble(0);
                                double lat = coordinates.getDouble(1);


                                earthquake.setPlace(properties.getString("place"));
                                earthquake.setType(properties.getString("type"));
                                earthquake.setTime(properties.getLong("time"));
                                earthquake.setMagnitude(properties.getDouble("mag"));
                                earthquake.setDetailLink(properties.getString("detail"));


                                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                                String formattedDate = dateFormat.format(new Date(Long.valueOf(properties.getLong("time"))).getTime());

                                //Finishe at part three. Need to start part four
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
}




