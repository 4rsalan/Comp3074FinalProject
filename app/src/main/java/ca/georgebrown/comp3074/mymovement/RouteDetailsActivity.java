package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
        final String date = getIntent().getExtras().getString("RouteDate");
        final Double distance = getIntent().getExtras().getDouble("RouteDistance");
        final Double rating = getIntent().getExtras().getDouble("RouteRating");
        final String tags = getIntent().getExtras().getString("RouteTags");



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

        // MAPS !!! OMG!!
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                i.putExtra("RouteId", id);
                startActivity(i);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create Json to share
                JSONObject jsonRoute = new JSONObject();
                JSONArray jsonPoints = new JSONArray();
                try {
                    jsonRoute.put("name", name);
                    jsonRoute.put("date", date);
                    jsonRoute.put("distance", distance);
                    jsonRoute.put("rating", rating);
                    jsonRoute.put("tags",tags);

                    points = MainActivity.dbHelper.getAllMapData(id);
                    for (int i = 0; i < points.size(); i++){
                        JSONObject jsonPoint = new JSONObject();
                        jsonPoint.put("latitude", points.get(i).getLatitude());
                        jsonPoint.put("longitude", points.get(i).getLongitude());
                        jsonPoint.put("timestamp", points.get(i).getTimestamp());
                        jsonPoints.put(jsonPoint);
                    }
                    jsonRoute.put("points", jsonPoints);

                    //Write JSON to file
                    Context context = getApplication();
                    File dir = context.getDir("data", Context.MODE_PRIVATE);
                    File jsonFile = new File( dir, "share_route.json");
                    FileWriter out = new FileWriter(jsonFile);
                    out.write(jsonRoute.toString());
                    out.close();

                    // Send Email
                    //Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", jsonFile);
                    Uri uri = Uri.parse(jsonFile.getAbsolutePath());
                    Log.d("EMAIL", uri.toString());
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("message/rfc822");
                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Route " + name);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Here is a new route");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(emailIntent, "Send Route"));

                } catch (JSONException e) {
                    //TODO Error handling
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplication(),"Error File Couldn't be Found", Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        int id = getIntent().getExtras().getInt("RouteId");
        points = MainActivity.dbHelper.getAllMapData(id);
        RoutePointClass start_point = points.get(0);
        LatLng start_cord = new LatLng(start_point.getLatitude(), start_point.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start_cord,20));
        RoutePointClass previous = start_point;
        for (int i = 1; i < points.size(); i++){
            RoutePointClass current = points.get(i);
            mMap.addPolyline(new PolylineOptions().add(new LatLng(previous.getLatitude(), previous.getLongitude()),
                    new LatLng(current.getLatitude(), current.getLongitude())).width(5).color(Color.RED));
            Log.d("ROUTES_DB", "Route Point with id: " + current.getId() + " has been mapped");
            previous = current;
        } // */
    }
}
