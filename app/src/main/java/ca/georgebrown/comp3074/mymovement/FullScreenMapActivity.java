package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class FullScreenMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<RoutePointClass> points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int id = getIntent().getExtras().getInt("RouteId");
        points = MainActivity.dbHelper.getAllMapData(id);
        RoutePointClass start_point = points.get(0);
        LatLng start_cord = new LatLng(start_point.getLatitude(), start_point.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start_cord,20));
        RoutePointClass previous = start_point;
        Toast.makeText(this, "Route start = " + start_point.getId(), Toast.LENGTH_LONG).show();
        for (int i = 1; i < points.size(); i++){
            RoutePointClass current = points.get(i);
            mMap.addPolyline(new PolylineOptions().add(new LatLng(previous.getLatitude(), previous.getLongitude()),
                    new LatLng(current.getLatitude(), current.getLongitude())).width(5).color(Color.RED));
            //Log.d("ROUTES_DB", "Route Point with id: " + current.getId() + " has been mapped");
            previous = current;
        }
    }
}
