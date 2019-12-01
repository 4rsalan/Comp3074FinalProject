package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RecordRouteActivity extends AppCompatActivity implements SensorEventListener {
    Boolean recording;
    SensorManager manager;
    Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_route);

        recording = false;
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        final Button btnStop = findViewById(R.id.btnStopRoute);
        final Button btnSave = findViewById(R.id.btnSaveRoute);
        final TextView lblRec = findViewById(R.id.lblRecoring);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recording) {
                    btnSave.setEnabled(true);
                    lblRec.setText("Not Recording");
                    btnStop.setText("Continue");
                    recording = false;

                    //stop the sensor
                    manager.unregisterListener(RecordRouteActivity.this);
                }
                else{
                    btnSave.setEnabled(false);
                    lblRec.setText("Recording Route");
                    btnStop.setText("Stop");
                    recording = true;

                    //start the sensor listener
                    if (sensor != null){
                        manager.registerListener(RecordRouteActivity.this, sensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
                    }
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String routeName = "New Route";

                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat formater = new SimpleDateFormat("mm/dd/yyyy");
                String dateCreated = formater.format(today);

                double distance = (new Random().nextInt(99)) + new Random().nextDouble();

                RouteClass route = new RouteClass(routeName, dateCreated, distance, 0.0, "");
                MainActivity.dbHelper.addItem(DbContract.RouteEntity.TABLE_NAME, route);
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                RoutePointClass point = new RoutePointClass(0, 0, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], sensorEvent.timestamp);
                Log.d("SENSOR", point.toString());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
