package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Color;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class RecordRouteActivity extends FragmentActivity implements FetchAddressClass.OnTaskCompleted, OnMapReadyCallback {
    Boolean recording;
    double distance;
    double speed;
    final int MAP_UPDATE_INTERVAL = 3000; // 3 seconds
    private GoogleMap mMap;

    List<RoutePointClass> list = new ArrayList<RoutePointClass>();

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_route);
        final TextView txtDistance = findViewById(R.id.lblDistance);
        final TextView txtSpeed = findViewById(R.id.lblSpeed);
        final TextView txtAddress = findViewById(R.id.lblAddress);
        distance = 0;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location!= null) {
                    RoutePointClass point = new RoutePointClass(0, 0, location.getLatitude(), location.getLongitude(), (double)location.getTime());
                    Log.d("LOCATION", point.toString());
                    if (mMap!=null){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(point.getLatitude(), point.getLongitude())));
                    }
                    list.add(point);
                    if (list.size() > 1){
                        RoutePointClass previous = list.get(list.size() -2);
                        mMap.addPolyline(new PolylineOptions().add(new LatLng(previous.getLatitude(), previous.getLongitude()),
                                new LatLng(point.getLatitude(), point.getLongitude()))
                                .width(5).color(Color.RED));

                        //calculate distance traveled in meters
                        float delta_dist[] = new float[1];
                        Location.distanceBetween(previous.getLatitude(),previous.getLongitude(), point.getLatitude(), point.getLongitude(), delta_dist );
                        distance += delta_dist[0];
                        speed = (delta_dist[0]) / ((point.getTimestamp() - previous.getTimestamp())/3600 );
                        txtDistance.setText(String.format("%f m", distance));
                        txtSpeed.setText(String.format("%f km/h", speed));
                    }
                    //uncomment when ready to find address
                    try {
                        String address = new FetchAddressClass(RecordRouteActivity.this, RecordRouteActivity.this).execute(location).get();
                        txtAddress.setText(address);
                    } catch (InterruptedException e) {
                        //do something
                    } catch (ExecutionException e) {
                        //do something
                    }
                }
            }
        };

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        locationRequest.setInterval(MAP_UPDATE_INTERVAL);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng toronto = new LatLng(43.67, -49.42);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto,20));
    }
}
