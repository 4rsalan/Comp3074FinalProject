package ca.georgebrown.comp3074.mymovement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ManageRoutesActivity extends AppCompatActivity {

    //set Static/public/final variable
    public static List<RouteClass> list = new ArrayList<RouteClass>();
    static ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_routes);

        MainActivity.dbHelper.getAllRoutes();

        //Point variables to UI objects
        ListView listRoutes = findViewById(R.id.listRoutes);
        final EditText etxtSearchRoute = findViewById(R.id.etxtSearchRoute);
        Button btnSearch = findViewById(R.id.btnSearch);

        //set the List adapater
        adapter = new ListAdapter(this, R.layout.route_item_layout, list);
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
                intent.putExtra("RouteName", route.getName());
                intent.putExtra("RouteRating", route.getRating());
                intent.putExtra("RouteTags", route.getTags());
                startActivityForResult(intent, 1);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = etxtSearchRoute.getText().toString();
                if (search.length() > 0 && search.trim() != "") {
                    MainActivity.dbHelper.getAllFilteredRoutes(etxtSearchRoute.getText().toString());
                    adapter.notifyDataSetChanged();
                }
                else {
                    MainActivity.dbHelper.getAllRoutes();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
    }
}
