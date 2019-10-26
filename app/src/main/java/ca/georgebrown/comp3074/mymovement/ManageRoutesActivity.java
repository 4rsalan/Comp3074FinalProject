package ca.georgebrown.comp3074.mymovement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ManageRoutesActivity extends AppCompatActivity {

    //set Static/public/final variables
    public static final ArrayList<RouteClass> list = new ArrayList<RouteClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_routes);

        //Point variables to UI objects
        ListView listRoutes = findViewById(R.id.listRoutes);

        //set list values
        for (int i = 1; i <= 10; i++){
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            String today = format.format(date);
            Double distance = i * 10.0;
            RouteClass route = new RouteClass(i, today, distance);
            list.add(route);
        }

        //set the List adapater
        ListAdapter adapter = new ListAdapter(this, R.layout.route_item_layout, list);
        listRoutes.setAdapter(adapter);

        //Set action events
        listRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), RouteDetailsActivity.class);
                RouteClass route = (RouteClass) adapterView.getItemAtPosition(i);
                intent.putExtra("RouteId", route.getId());
                intent.putExtra("RouteDate", route.getDate());
                intent.putExtra("RouteDistance", route.getDistance());
                startActivity(intent);
            }
        });
    }
}
