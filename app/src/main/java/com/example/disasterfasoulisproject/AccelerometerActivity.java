package com.example.disasterfasoulisproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    public static final long START_TIME_IN_MILLIS = 30000;
    public long timeLeftInMillis = START_TIME_IN_MILLIS;



    private  Button btnPlayMusic,btnStartStopTime;
    TextView txtTimeCountDown;
    private SensorManager sensorManager;
    private MediaPlayer mediaPlayer;

    private CountDownTimer countDownTimer;
    private boolean timerRunning;

    Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        txtTimeCountDown = findViewById(R.id.txtTime);

        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(AccelerometerActivity.this,R.raw.count );

        btnPlayMusic = findViewById(R.id.btnPlay);
        btnStartStopTime = findViewById(R.id.btnStartPause);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) AccelerometerActivity.this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        btnPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    pauseMusic();
                }else {
                    playMusic();
                }
            }
        });

        btnStartStopTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning){
                    pauseTimer();
                }else {
                    startTimer();
                }
            }
        });

        updateCountDownText();
        
    }

    //------------------------------Methods for CountDown--------------------------------------------//

    private void  pauseTimer(){

        countDownTimer.cancel();
        timerRunning = false;
        btnStartStopTime.setText("Start");
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                timerRunning = false;
                btnStartStopTime.setText("Start");
                btnStartStopTime.setVisibility(View.INVISIBLE);
            }
        }.start();

        timerRunning = true;
        btnStartStopTime.setText("pause");
    }

    public  void updateCountDownText(){
        int minutes = (int) timeLeftInMillis/1000/60;
        int seconds = (int) (timeLeftInMillis/1000) % 60;

        String timeLeftFormated = String.format("%02d:%02d",minutes,seconds);
        txtTimeCountDown.setText(timeLeftFormated);
    }



    //----------------------------------------------------------------------------------------------//


    //------------------------------------Stop and Start Music Music----------------------------------//
    public void pauseMusic(){
        if (mediaPlayer!=null){
            mediaPlayer.pause();
            btnPlayMusic.setText("Play");
        }
    }

    public void playMusic(){
        if (mediaPlayer!=null){
            mediaPlayer.start();
            btnPlayMusic.setText("Pause");
        }
    }
    //------------------------------------Stop and Start Music Music----------------------------------//

    //-----------------------In Case Activity Is Destroyed and Music is Playing------------------------//

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.pause();
            mediaPlayer.release();
        }
    }

    //------------------------------------------------------------------------------------------------//

    //--------------Methods Implemented from SensorEventListener Interface-----------------------------//

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
    //---------------------------------------------------------------------------------------------------//

    public void AlertDialog(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(AccelerometerActivity.this);
        alert.setMessage("Do you want to Cancell Alert ? ").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alrt = alert.create();
        alrt.setTitle("Fall Detected");
        alrt.show();
    }
}