package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class RouteDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    Boolean editing;
    private GoogleMap mMap;
    List<RoutePointClass> points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editing = false;

        setContentView(R.layout.activity_route_details);

        //set Extras to Variables
        final int id = getIntent().getExtras().getInt("RouteId");
        final String name = getIntent().getExtras().getString("RouteName");
        String date = getIntent().getExtras().getString("RouteDate");
        Double distance = getIntent().getExtras().getDouble("RouteDistance");
        final Double rating = getIntent().getExtras().getDouble("RouteRating");
        final String tags = getIntent().getExtras().getString("RouteTags");

        //get route points
        points = MainActivity.dbHelper.getAllMapData(id);

        //set Variables to UI objects
        ImageButton btnEdit = findViewById(R.id.btnEdit);
        ImageButton btnDel = findViewById(R.id.btnDelete);
        ImageButton btnShare = findViewById(R.id.btnShare);
        Button btnExpand = findViewById(R.id.btnExpend);
        TextView txtRDTitle = findViewById(R.id.txtRDTitle);
        final EditText etxtRouteName = findViewById(R.id.etxtRouteName);
        final EditText etxtTags = findViewById(R.id.etxtTags);
        TextView txtDateCreated = findViewById(R.id.txtDateCreated);
        TextView txtDistanceWalked = findViewById(R.id.txtDistanceWalked);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final Button saveBtn = findViewById(R.id.saveBtn);


        //set Text to UI objects
        txtRDTitle.setText("Details Of Route " + id);
        etxtRouteName.setText(name);
        txtDateCreated.setText("Date Created: " + date);
        txtDistanceWalked.setText("Distance Walked: " + distance);
        ratingBar.setRating(rating.floatValue());
        etxtTags.setText(tags);


        etxtRouteName.setEnabled(false);
        etxtTags.setEnabled(false);
        ratingBar.setEnabled(false);
        saveBtn.setVisibility(saveBtn.GONE);


        //Set Action Events
        btnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), FullScreenMapActivity.class);
                startActivity(i);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ShareRouteActivity.class);
                startActivity(i);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editing = !editing;
                if(editing){
                    saveBtn.setVisibility(saveBtn.VISIBLE);
                    etxtRouteName.setEnabled(true);
                    etxtTags.setEnabled(true);
                    ratingBar.setEnabled(true);
                }
                else{
                    MainActivity.dbHelper.updateItem(DbContract.RouteEntity.TABLE_NAME, etxtRouteName.getText().toString(), etxtTags.getText().toString(), ratingBar.getRating(), id);
                    saveBtn.setVisibility(saveBtn.GONE);
                    etxtRouteName.setEnabled(false);
                    etxtTags.setEnabled(false);
                    ratingBar.setEnabled(false);
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity.dbHelper.updateItem(DbContract.RouteEntity.TABLE_NAME, etxtRouteName.getText().toString(), etxtTags.getText().toString(), ratingBar.getRating(), id);
                editing = false;
                saveBtn.setVisibility(saveBtn.GONE);
                etxtRouteName.setEnabled(false);
                etxtTags.setEnabled(false);
                ratingBar.setEnabled(false);
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.dbHelper.deleteItem(DbContract.RouteEntity.TABLE_NAME, id);
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        RoutePointClass start_point = points.get(0);
        LatLng start_cord = new LatLng(start_point.getLatitude(), start_point.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start_cord,20));
        RoutePointClass previous = start_point;
        for (int i = 1; i < points.size(); i++){
            RoutePointClass current = points.get(i);
            mMap.addPolyline(new PolylineOptions().add(new LatLng(previous.getLatitude(), previous.getLongitude()),
            new LatLng(current.getLatitude(), current.getLongitude()))
            .width(5).color(Color.RED));
        }

    }
}
