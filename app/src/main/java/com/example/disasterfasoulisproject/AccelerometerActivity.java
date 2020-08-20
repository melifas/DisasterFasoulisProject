package com.example.disasterfasoulisproject;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) AccelerometerActivity.this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
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
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}