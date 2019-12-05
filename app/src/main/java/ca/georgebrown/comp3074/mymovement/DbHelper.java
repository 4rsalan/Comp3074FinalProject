package ca.georgebrown.comp3074.mymovement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "routes.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.RouteEntity.SQL_CREATE);
        Log.d("ROUTES_DB", "Database created with routes table");
        db.execSQL(DbContract.MapDataEntity.SQL_CREATE);
        Log.d("ROUTES_DB", "Database updated with map_data table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL(DbContract.RouteEntity.SQL_DROP);
        Log.d("ROUTES_DB", "routes table dropped");
        db.execSQL(DbContract.MapDataEntity.SQL_DROP);
        Log.d("ROUTES_DB", "map_data table dropped");
        onCreate(db);
    }

    public void addItem(String table, RouteClass route, List<RoutePointClass> points) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        if (table == DbContract.RouteEntity.TABLE_NAME) {
            cv.put(DbContract.RouteEntity.COLUMN_ROUTE, route.name);
            cv.put(DbContract.RouteEntity.COLUMN_DATE, route.date);
            cv.put(DbContract.RouteEntity.COLUMN_DISTANCE, route.distance);
            cv.put(DbContract.RouteEntity.COLUMN_RATING, route.rating);
            cv.put(DbContract.RouteEntity.COLUMN_TAG, route.tags);
        }

        long result = db.insert(table, null, cv);
        int index = 0;

        Log.d("ROUTES_DB", "Item with id " + result + " has been inserted into " + table + " table");

        while (index < points.size()){
            cv = new ContentValues();
            cv.put(DbContract.MapDataEntity.COLUMN_ROUTE_ID, result);
            cv.put(DbContract.MapDataEntity.COLUMN_LATITUDE, points.get(index).latitude);
            cv.put(DbContract.MapDataEntity.COLUMN_LONGITUDE, points.get(index).longitude);
            cv.put(DbContract.MapDataEntity.COLUMN_TIMESTAMP, points.get(index).longitude);
            long pointResult = db.insert(DbContract.MapDataEntity.TABLE_NAME, null, cv);
            Log.d("ROUTES_DB", "Route Point with id: " + pointResult + " has been inserted into " + DbContract.MapDataEntity.TABLE_NAME + " table");
            index++;
        }
    }

    public int updateItem(String table, String name, String tags, Float rating, int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        String whereClause = DbContract.RouteEntity._ID + "=" + id;

        cv.put(DbContract.RouteEntity.COLUMN_ROUTE, name);
        cv.put(DbContract.RouteEntity.COLUMN_RATING, rating);
        cv.put(DbContract.RouteEntity.COLUMN_TAG, tags);

        int result = db.update(table, cv, whereClause,null);
        if ((result==0))
            Log.d("ROUTES_DB", "Route with id " + id + " could not be edited");
        else
            Log.d("ROUTES_DB", "Route with id " + id + " was edited");
        return result;
    }

    public void deleteItem(String table, int id) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = DbContract.RouteEntity._ID + "=" + id;
        String pointWhereClause = DbContract.MapDataEntity.COLUMN_ROUTE_ID + "=" + id;

        int result = db.delete(table, whereClause, null);
        if ((result==0))
            Log.d("ROUTES_DB", "Route with id " + id + " could not be deleted");
        else
            Log.d("ROUTES_DB", "Route with id " + id + " was deleted");

        result = db.delete(DbContract.MapDataEntity.TABLE_NAME, pointWhereClause, null);
        if((result==0))
            Log.d("ROUTES_DB", "Map Data could not be deleted");
        else
            Log.d("ROUTES_DB", "All map data associated with Route Id " + id + " has been deleted");

    }

    public void getAllRoutes() {
        SQLiteDatabase db = getReadableDatabase();

        //define query arguments (most are null)
        String[] columns = {
                DbContract.RouteEntity._ID,
                DbContract.RouteEntity.COLUMN_ROUTE,
                DbContract.RouteEntity.COLUMN_DATE,
                DbContract.RouteEntity.COLUMN_DISTANCE,
                DbContract.RouteEntity.COLUMN_RATING,
                DbContract.RouteEntity.COLUMN_TAG
        };
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        //get the data
        Cursor data = db.query(DbContract.RouteEntity.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
       /* if (data.getCount() == 0){
            createDummyData();
            data = db.query(DbContract.RouteEntity.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
        }*/

        RouteClass route;
        int count = 0;
        ManageRoutesActivity.list.clear();

        while (data.moveToNext()){
            int id = data.getInt(data.getColumnIndexOrThrow(DbContract.RouteEntity._ID));
            String name = data.getString(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_ROUTE));
            String date = data.getString(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_DATE));
            Double distance = data.getDouble(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_DISTANCE));
            Double rating = data.getDouble(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_RATING));
            String tags = data.getString(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_TAG));

            route = new RouteClass(name, date, distance, rating, tags);
            route.setId(id);
            ManageRoutesActivity.list.add(route);
            count += 1;
        }

        //log the number of items in database
        Log.d("ROUTES_DB", "There are " + count + " items in the Database");
    }

    public void getAllFilteredRoutes(String tagString){
        SQLiteDatabase db = getReadableDatabase();

        //define query arguments (most are null)
        String[] columns = {
                DbContract.RouteEntity._ID,
                DbContract.RouteEntity.COLUMN_ROUTE,
                DbContract.RouteEntity.COLUMN_DATE,
                DbContract.RouteEntity.COLUMN_DISTANCE,
                DbContract.RouteEntity.COLUMN_RATING,
                DbContract.RouteEntity.COLUMN_TAG
        };
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        Cursor data = db.query(DbContract.RouteEntity.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        RouteClass route;
        int count = 0;
        ManageRoutesActivity.list.clear();

        while (data.moveToNext()){
            int id = data.getInt(data.getColumnIndexOrThrow(DbContract.RouteEntity._ID));
            String name = data.getString(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_ROUTE));
            String date = data.getString(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_DATE));
            Double distance = data.getDouble(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_DISTANCE));
            Double rating = data.getDouble(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_RATING));
            String tags = data.getString(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_TAG));

            String[] searchTags =  TagStringSeparator(tagString);
            String[] routeTags = TagStringSeparator(tags);

            //only add to list if tag is found
            for (int i = 0; i < searchTags.length; i++) {
                if (Arrays.asList(routeTags).contains(searchTags[i])) {
                    route = new RouteClass(name, date, distance, rating, tags);
                    route.setId(id);
                    ManageRoutesActivity.list.add(route);
                    count += 1;
                }
            }
        }

        Log.d("ROUTES_DB", count + " results match the search tags");
    }

    private static String[] TagStringSeparator(String tags){
        String tagsNoWhiteSpace = tags.replaceAll(" ", "");
        String[] tagArray = tagsNoWhiteSpace.split(",");
        return tagArray;
    }

    public List<RoutePointClass> getAllMapData(int id) {
        List<RoutePointClass> points = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        //define query arguments (most are null)
        String[] columns = {
                DbContract.MapDataEntity._ID,
                DbContract.MapDataEntity.COLUMN_ROUTE_ID,
                DbContract.MapDataEntity.COLUMN_LATITUDE,
                DbContract.MapDataEntity.COLUMN_LONGITUDE,
                DbContract.MapDataEntity.COLUMN_TIMESTAMP
        };
        String selection = DbContract.MapDataEntity.COLUMN_ROUTE_ID + "= ?";
        String[] selectionArgs = {String.valueOf(id)};
        String groupBy = null;
        String having = null;
        String orderBy = DbContract.MapDataEntity.COLUMN_TIMESTAMP;

        Cursor data = db.query(DbContract.MapDataEntity.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (data.moveToNext()){
            int point_id = data.getInt(data.getColumnIndexOrThrow(DbContract.MapDataEntity._ID));
            int route_id = data.getInt(data.getColumnIndexOrThrow(DbContract.MapDataEntity.COLUMN_ROUTE_ID));
            Double lat = data.getDouble(data.getColumnIndexOrThrow(DbContract.MapDataEntity.COLUMN_LATITUDE));
            Double lng = data.getDouble(data.getColumnIndexOrThrow(DbContract.MapDataEntity.COLUMN_LONGITUDE));
            Double ts = data.getDouble(data.getColumnIndexOrThrow(DbContract.MapDataEntity.COLUMN_TIMESTAMP));
            points.add(new RoutePointClass(point_id,route_id,lat,lng,ts));
        }
        return points;
    }
/*
    public void createDummyData(){
        RouteClass route = new RouteClass("Route 1", "11/11/2019", 12.05, 4.0, "");
        List<RoutePointClass> points = new ArrayList<RoutePointClass>();
        addItem(DbContract.RouteEntity.TABLE_NAME, route, points);
        route = new RouteClass("Route 2", "11/02/2019", 3.45, 3.5, "Store");
        addItem(DbContract.RouteEntity.TABLE_NAME, route, points);
        route = new RouteClass("Route 3", "10/15/2019", 6.94, 3.0, "");
        addItem(DbContract.RouteEntity.TABLE_NAME, route, points);
        route = new RouteClass("Route 4", "9/14/2019", 4.02, 1.5, "Old");
        addItem(DbContract.RouteEntity.TABLE_NAME, route, points);
        route = new RouteClass("Route 5", "8/29/2019", 8.29, 5.0, "Vacation/Amusement Park");
        addItem(DbContract.RouteEntity.TABLE_NAME, route, points);
    }

 */
}
