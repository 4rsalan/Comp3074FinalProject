package ca.georgebrown.comp3074.mymovement;

public class RouteClass {
    public int id;
    public String date;
    public Double distance;

    public RouteClass(int i, String da, Double di){
        id = i;
        date = da;
        distance = di;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
