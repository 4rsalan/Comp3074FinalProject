package ca.georgebrown.comp3074.mymovement;

import java.util.List;

public class RouteClass {
    public int id;
    public String name;
    public String date;
    public Double distance;
    public Double rating;
    public String tags;
    public List<MapDataClass> mapData;

    public RouteClass(String n, String da, Double di, Double r, String t){
        name = n;
        date = da;
        distance = di;
        rating = r;
        tags = t;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
