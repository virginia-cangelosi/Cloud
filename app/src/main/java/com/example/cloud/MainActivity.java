package com.example.cloud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements MotionSensorManager.OnMotionSensorManagerListener{

    private MotionSensorManager mMotionSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMotionSensorManager = new MotionSensorManager(this);
        mMotionSensorManager.setOnMotionSensorManagerListener(this);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("message");
        myRef.setValue("Hello World!");

        User user = new User("Mickey",18);
        DatabaseReference myRef1 = db.getReference().child("user");
        myRef1.setValue(user);
    }

    @Override
    public void onAccValueUpdated(float[] accelerationVal) {
    }

    @Override
    public void onGyroValueUpdated(float[] gyroscopeVal) {
    }

    @Override
    public void onMagValueUpdated(float[] magnetometerVal) {

    }

    @Override
    public void onPause() {
        super.onPause();

        mMotionSensorManager.unregisterMotionSensors();
    }

    @Override
    public void onResume() {
        super.onResume();

        mMotionSensorManager.registerMotionSensors();
    }
}