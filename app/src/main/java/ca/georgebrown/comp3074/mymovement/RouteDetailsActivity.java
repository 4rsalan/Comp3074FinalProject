package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

public class RouteDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        //set Extras to Variables
        final int id = getIntent().getExtras().getInt("RouteId");
        String name = getIntent().getExtras().getString("RouteName");
        String date = getIntent().getExtras().getString("RouteDate");
        Double distance = getIntent().getExtras().getDouble("RouteDistance");
        Double rating = getIntent().getExtras().getDouble("RouteRating");
        String tags = getIntent().getExtras().getString("RouteTags");

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
        RatingBar ratingBar = findViewById(R.id.ratingBar);


        //set Text to UI objects
        txtRDTitle.setText("Details Of Route " + id);
        etxtRouteName.setText(name);
        txtDateCreated.setText("Date Created: " + date);
        txtDistanceWalked.setText("Distance Walked: " + distance);
        ratingBar.setRating(rating.floatValue());
        etxtTags.setText(tags);


        etxtRouteName.setEnabled(false);
        etxtTags.setEnabled(false);

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
                etxtRouteName.setEnabled(true);
                etxtTags.setEnabled(true);
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.dbHelper.deleteItem(DbContract.RouteEntity.TABLE_NAME, id);
                finish();
            }
        });
    }
}
