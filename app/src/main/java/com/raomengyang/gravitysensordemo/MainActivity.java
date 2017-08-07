package com.raomengyang.gravitysensordemo;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.tv_main)
    ImageView tvMain;

    //    private SensorBackgroundView sensorBackgroundView;
    private SensorManager mSensorManager;
    private WindowManager mWindowManager;
    private Sensor mAccelerator;
    private Display mDisplay;

    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initComponent();

//        sensorBackgroundView.setBackgroundResource(R.color.colorPrimary);
//        setContentView(sensorBackgroundView);

    }


    @Override
    protected void onResume() {
        super.onResume();
        startSensorSimulator();
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopSensorSimulator();
    }

    private void initComponent() {
        initSensor();
        // Get an instance of the WindowManager
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
//        sensorBackgroundView = new SensorBackgroundView(this);
    }

    private void initSensor() {
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        mAccelerator = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


    private void startSensorSimulator() {
        mSensorManager.registerListener(this, mAccelerator, SensorManager.SENSOR_DELAY_GAME);
    }

    private void stopSensorSimulator() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

             /*
             * record the accelerometer data, the event's timestamp as well as
             * the current time. The latter is needed so we can calculate the
             * "present" time during rendering. In this application, we need to
             * take into account how the screen is rotated with respect to the
             * sensors (which always return data in a coordinate space aligned
             * to with the screen in its native orientation).
             */
        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                mSensorX = event.values[0];
                mSensorY = event.values[1];
//                    mSensorZ = event.values[2];
                break;
            case Surface.ROTATION_90:
                mSensorX = -event.values[1];
                mSensorY = event.values[0];
                break;
            case Surface.ROTATION_180:
                mSensorX = -event.values[0];
                mSensorY = -event.values[1];
                break;
            case Surface.ROTATION_270:
                mSensorX = event.values[1];
                mSensorY = -event.values[0];
                break;
        }

        Log.e(TAG, String.format("mSensorX = %s , mSensorY = %s", mSensorX, mSensorY));
        tvMain.setTranslationX(mSensorX * 10);
        tvMain.setTranslationY(mSensorY * 10);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    class SensorBackgroundView extends FrameLayout {
        private TextView tvMain;


        public SensorBackgroundView(@NonNull Context context) {
            super(context);

//            mTextView = (TextView) findViewById(R.id.tv_main);
            mAccelerator = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.e(TAG, String.format("mSensorX = %s , mSensorY = %s", mSensorX, mSensorY));
//            tvMain.setTranslationX(mSensorX);
//            tvMain.setTranslationY(mSensorY);
            invalidate();
        }
    }


}
