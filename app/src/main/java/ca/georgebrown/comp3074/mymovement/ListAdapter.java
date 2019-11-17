package ca.georgebrown.comp3074.mymovement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListAdapter extends ArrayAdapter<RouteClass> {
    int layoutId;
    public ListAdapter(@NonNull Context context, int resource, @NonNull List<RouteClass> objects){
        super(context, resource, objects);
        layoutId = resource;
    }

    @NonNull
    @Override
    public View getView(int Position, @Nullable View newView, @NonNull ViewGroup parent){
        if (newView==null){
            LayoutInflater inflate = LayoutInflater.from(getContext());
            newView = inflate.inflate(layoutId, null);
        }

        //Point Variables to UI objects
        TextView txtRouteItem = newView.findViewById(R.id.txtRouteItem);
        TextView txtTags = newView.findViewById(R.id.txtTags);

        //Set the Item's Text
        String msg = getItem(Position).name;
        txtRouteItem.setText(msg);
        msg = getItem(Position).tags;
        txtTags.setText("Tags: " + msg);

        //Return the view
        return newView;
    }
}
