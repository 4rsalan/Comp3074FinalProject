package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RecordRouteActivity extends AppCompatActivity implements FetchAddressClass.OnTaskCompleted{
    Boolean recording;

    List<RoutePointClass> list = new ArrayList<RoutePointClass>();

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_route);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location!= null) {
                    RoutePointClass point = new RoutePointClass(0, 0, location.getLatitude(), location.getLongitude(), location.getTime());
                    Log.d("LOCATION", point.toString());
                    list.add(point);
                    //uncomment when ready to find address
                    //new FetchAddressClass(RecordRouteActivity.this, RecordRouteActivity.this).execute(location);
                }
            }
        };

        recording = false;

        final Button btnStop = findViewById(R.id.btnStopRoute);
        final Button btnSave = findViewById(R.id.btnSaveRoute);
        final Button btnRestart = findViewById(R.id.btnRestartRoute);
        final TextView lblRec = findViewById(R.id.lblRecoring);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recording) {
                    btnSave.setEnabled(true);
                    btnRestart.setEnabled(true);
                    lblRec.setText("Not Recording");
                    btnStop.setText("Continue");
                    recording = false;
                    onPause();
                }
                else{
                    btnSave.setEnabled(false);
                    btnRestart.setEnabled(false);
                    lblRec.setText("Recording Route");
                    btnStop.setText("Stop");
                    recording = true;
                    onResume();
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
                MainActivity.dbHelper.addItem(DbContract.RouteEntity.TABLE_NAME, route, list);
                list.clear();
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
            }
        });
    }

    private void startTracking(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopTracking(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onPause() {
        stopTracking();
        super.onPause();
    }

    @Override
    protected void onResume() {
        startTracking();
        super.onResume();
    }

    @Override
    public void onTaskCompleted(String result) {

    }
}
