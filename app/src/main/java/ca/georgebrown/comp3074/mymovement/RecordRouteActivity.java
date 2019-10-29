package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    }
}
