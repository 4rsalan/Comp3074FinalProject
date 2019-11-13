package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RecordRouteActivity extends AppCompatActivity {
    Boolean recording;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_route);

        recording = true;

        final Button btnStop = findViewById(R.id.btnStopRoute);
        final Button btnSave = findViewById(R.id.btnSaveRoute);
        final TextView lblRec = findViewById(R.id.lblRecoring);

        btnSave.setEnabled(false);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recording) {
                    btnSave.setEnabled(true);
                    lblRec.setText("Route Paused");
                    btnStop.setText("Continue");
                    recording = false;
                }
                else{
                    btnSave.setEnabled(false);
                    lblRec.setText("Recording Route");
                    btnStop.setText("Stop");
                    recording = true;
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
}
