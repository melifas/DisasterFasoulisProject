package com.example.disasterfasoulisproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    public static final long START_TIME_IN_MILLIS = 30000;
    public long timeLeftInMillis = START_TIME_IN_MILLIS;


    //-------------------Object Created for countDown---------------------------------------------//
    CountDownTimer countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timeLeftInMillis = millisUntilFinished;
            updateCountDownText();

        }

        @Override
        public void onFinish() {
            timerRunning = false;
            //btnStartStopTime.setText("Start");
            //btnStartStopTime.setVisibility(View.INVISIBLE);
        }
    };

//--------------------------------------------------------------------------------------------------//

    private  Button btnPlayMusic,btnTestAlert, btnAbortAlarm;
    TextView txtTimeCountDown;
    private SensorManager sensorManager;
    private MediaPlayer mediaPlayer;

    //private CountDownTimer countDownTimer;
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
        //btnStartStopTime = findViewById(R.id.btnAbortAlarm);
        btnTestAlert = findViewById(R.id.btnTestAlertWindow);
        btnAbortAlarm = findViewById(R.id.btnAbortAlarm);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) AccelerometerActivity.this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        btnAbortAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer!=null && countDownTimer!= null) {
                    mediaPlayer.pause();
                    mediaPlayer.release();
                    //pauseTimer();
                    countDownTimer.cancel();
                    txtTimeCountDown.setText("00:00");
                }
                /*AccelerometerActivity.this.finish();
                startActivity(new Intent(AccelerometerActivity.this,MainActivity.class));*/
            }
        });

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

        /*btnStartStopTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning){
                    pauseTimer();
                }else {
                    startTimer();
                }
            }
        });*/

        //updateCountDownText();
        
    }

    //------------------------------Methods for CountDown--------------------------------------------//

    private void  pauseTimer(){
        countDownTimer.cancel();
        timerRunning = false;
        txtTimeCountDown.setText("00:00");
        //btnStartStopTime.setText("Start");
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
                //btnStartStopTime.setText("Start");
                //btnStartStopTime.setVisibility(View.INVISIBLE);
            }
        }.start();

        timerRunning = true;
        //btnStartStopTime.setText("pause");
    }

    public  void updateCountDownText(){
        int minutes = (int) timeLeftInMillis/1000/60;
        int seconds = (int) (timeLeftInMillis/1000) % 60;

        String timeLeftFormated = String.format("%02d:%02d",minutes,seconds);
        txtTimeCountDown.setText(timeLeftFormated);
    }



    //----------------------------------------------------------------------------------------------//


    //------------------------------------Stop and Start Music----------------------------------//
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
        pauseTimer();
        txtTimeCountDown.setText("00:00");
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
                playMusic();
                //startTimer();
                countDownTimer.start();
            }
            
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    //---------------------------------------------------------------------------------------------------//

    public void AlertDialog(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Fall Detected")
                .setMessage("Do you really want to cancel the Alarm?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: Add positive button action code here
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            private static final int AUTO_DISMISS_MILLIS = 30000;
            @Override
            public void onShow(final DialogInterface dialog) {
                final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                final CharSequence negativeButtonText = defaultButton.getText();
                playMusic();
                new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        defaultButton.setText(String.format(
                                Locale.getDefault(), "%s (%d)",
                                negativeButtonText,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero

                        ));
                    }
                    @Override
                    public void onFinish() {
                        if (((AlertDialog) dialog).isShowing()) {
                            dialog.dismiss();
                            pauseMusic();
                        }
                    }
                }.start();
            }
        });
        dialog.show();

    }


}