package ca.georgebrown.comp3074.mymovement;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressClass extends AsyncTask<Location, Void, String> {

    public interface OnTaskCompleted{
        void onTaskCompleted(String result);
    }

    private Context context;
    private OnTaskCompleted listener;

    FetchAddressClass(Context c, OnTaskCompleted l){
        context = c;
        listener = l;
    }

    @Override
    protected String doInBackground(Location... locations) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Location location = locations[0];
        List<Address> addressList = null;
        String result = "";

        try{
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        }
        catch (IOException e){
            result = "Service not available";
            Log.e("LOCATION", result);
        }
        catch (IllegalArgumentException i){
            result = "Invalid Position";
            Log.e("LOCATION", result);
        }

        if (addressList == null || addressList.size() == 0){
            result = "No addresses found for location";
            Log.d("LOCATION", result);
        }
        else {
            Address address = addressList.get(0);
            ArrayList<String> addressParts = new ArrayList<>();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                addressParts.add(address.getAddressLine(i));
            }
            result = TextUtils.join("\n", addressParts);
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onTaskCompleted(s);
        super.onPostExecute(s);
    }
}
