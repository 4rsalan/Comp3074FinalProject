package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create Database Helper Instance
        dbHelper = new DbHelper(this);

        //Point Variables to UI objects
        Button btnManageRoutes = findViewById(R.id.btnManageRoutes);
        Button btnAbout = findViewById(R.id.btnAbout);
        Button btnNewRoute = findViewById(R.id.btnNewRoute);



        //Set Action Events
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AboutActivity.class);
                startActivity(i);
            }
        });

        btnNewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RecordRouteActivity.class);
                startActivity(i);
            }
        });

        btnManageRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ManageRoutesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy(){
        dbHelper.close();
        super.onDestroy();
    }
}
