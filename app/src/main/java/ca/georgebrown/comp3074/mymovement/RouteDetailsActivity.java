package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class RouteDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        //set Extras to Variables
        int id = getIntent().getExtras().getInt("RouteId");
        String date = getIntent().getExtras().getString("RouteDate");
        Double distance = getIntent().getExtras().getDouble("RouteDistance");

        //set Variables to UI objects
        TextView txtRDTitle = findViewById(R.id.txtRDTitle);
        EditText etxtRouteName = findViewById(R.id.etxtRouteName);
        TextView txtDateCreated = findViewById(R.id.txtDateCreated);
        TextView txtDistanceWalked = findViewById(R.id.txtDistanceWalked);

        //set Text to UI objects
        txtRDTitle.setText("Details Of Route " + id);
        etxtRouteName.setText("Route " + id);
        txtDateCreated.setText("Date Created: " + date);
        txtDistanceWalked.setText("Distance Walked: " + distance);
    }
}
