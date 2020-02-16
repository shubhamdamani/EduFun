package com.example.droidrun;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    Button multiplayer,singleplayer,exi,more,affair;
//    Button inst;
    MediaPlayer media;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        multiplayer=findViewById(R.id.multi);
        singleplayer=findViewById(R.id.single);
//        inst=findViewById(R.id.inst);
        exi=findViewById(R.id.exit);
        more = findViewById(R.id.more);
        affair=findViewById(R.id.affair);
        media = MediaPlayer.create(this, R.raw.tada);
        affair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainMenu.this,Topic.class);
                startActivity(i);
            }
        });



        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainMenu.this,MainActivity.class);
                startActivity(i);
            }
        });

        singleplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainMenu.this,SinglePlayer.class);
                startActivity(i);
            }
        });

//        inst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(MainMenu.this,Instruction.class);
//                startActivity(i);
//
//            }
//        });

        exi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this,ObjectActivity.class);
                startActivity(intent);
            }
        });

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                media.start();
                Intent intent = new Intent(MainMenu.this,ObjectActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

}
