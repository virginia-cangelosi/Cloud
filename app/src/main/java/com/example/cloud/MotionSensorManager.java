package com.example.cloud;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MotionSensorManager implements SensorEventListener {

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //Information passed to the activities through the listener
    private OnMotionSensorManagerListener motionSensorManagerListener;
    //Sensor manager is used to access all sensors
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private Sensor magnetometer;

    /**
     * Constructor of the class. Initialises all the sensors
     */
    public MotionSensorManager(Context context){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    /**
     * Constructor of the class. Initialises all the sensors
     */
    public void setOnMotionSensorManagerListener(OnMotionSensorManagerListener motionSensorManagerListener){
        this.motionSensorManagerListener = motionSensorManagerListener;
    }

    /**
     * Listeners to know when the sensor data has changed
     */
    public interface OnMotionSensorManagerListener{
        void onAccValueUpdated(float[] accelerationVal);
        void onGyroValueUpdated(float[] gyroscopeVal);
        void onMagValueUpdated(float[] magnetometerVal);
    }

    private float gravity[] = new float[3];
    private double h;
    private float alpha =0.8f;
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                //alpha is t/(t+dT) where t=LPF time constant and dT is the event delivery rate
                gravity[0] = alpha * gravity[0] + (1-alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1-alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1-alpha) * event.values[2];

                float linear_acceleration [] = new float [3];
                linear_acceleration[0] = event.values[0] - gravity[0];
                linear_acceleration[1] = event.values[1] - gravity[1];
                linear_acceleration[2] = event.values[2] - gravity[2];

                motionSensorManagerListener.onAccValueUpdated(new float[]{linear_acceleration[0],
                        linear_acceleration[1],linear_acceleration[2]});
                break;

            case Sensor.TYPE_GYROSCOPE:
                motionSensorManagerListener.onGyroValueUpdated(new float[]{event.values[0],
                        event.values[1],event.values[2]});
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                h = Math.sqrt(event.values[0]*event.values[0] + event.values[1]*event.values[1] +
                        event.values[2]*event.values[2]);
                motionSensorManagerListener.onMagValueUpdated(new float[]{event.values[0],
                        event.values[1],event.values[2], (float) h});
                break;
        }

    }

    public void registerMotionSensors(){
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,gyroscope,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,magnetometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterMotionSensors(){
        sensorManager.unregisterListener(this);
    }
}
