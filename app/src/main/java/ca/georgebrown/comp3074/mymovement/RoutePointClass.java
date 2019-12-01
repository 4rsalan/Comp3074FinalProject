package ca.georgebrown.comp3074.mymovement;

import androidx.annotation.NonNull;

public class RoutePointClass {
    public int id;
    public int route_id;
    public double latitude;

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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public double longitude;
    public double altitude;
    public double timestamp;

    public RoutePointClass(int i, int ri, double la, double lo, double al, double t){
        id = i;
        route_id = ri;
        latitude = la;
        longitude = lo;
        altitude = al;
        timestamp = t;
    }

    @NonNull
    @Override
    public String toString() {
        return "Id: " + id + ", Route Id: " + route_id + ", Latitude: " + latitude + ", Longitude" + longitude + ", Altitude: " + altitude + ", Timestamp: " + timestamp;
    }
}
