package ca.georgebrown.comp3074.mymovement;

import androidx.annotation.NonNull;

public class RoutePointClass {
    public int id;
    public int route_id;
    public double latitude;
    public double longitude;
    public double timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public RoutePointClass(int id, int route_id, double latitude, double longitude, double timestamp){
        this.id = id;
        this.route_id = route_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "Id: " + id + ", Route Id: " + route_id + ", Latitude: " + latitude + ", Longitude: " + longitude + ", Timestamp: " + timestamp;
    }
}
