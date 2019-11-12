package ca.georgebrown.comp3074.mymovement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
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

    public void addItem(String table, RouteClass route) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DbContract.RouteEntity.COLUMN_ROUTE, route.name);
        cv.put(DbContract.RouteEntity.COLUMN_DATE, route.date);
        cv.put(DbContract.RouteEntity.COLUMN_DISTANCE, route.distance);
        cv.put(DbContract.RouteEntity.COLUMN_RATING, route.rating);
        cv.put(DbContract.RouteEntity.COLUMN_TAG, route.tags);

        long result = db.insert(table, null, cv);
        Log.d("ROUTES_DB", "Item with id " + result + "has been inserted into " + table + " table");
    }

    public int updateItem(String table, RouteClass route) {
        return 1; //replace
    }

    public int deleteItem(String table, int id) {
        return 1; //replace
    }

    public List<RouteClass> getAllRoutes() {
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
        if (data.getCount() == 0){
            createDummyData();
            data = db.query(DbContract.RouteEntity.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
        }

        RouteClass route;
        int count = 0;
        ManageRoutesActivity.list.clear();
        List<RouteClass> tempList = new ArrayList<RouteClass>();

        while (data.moveToNext()){
            int id = data.getInt(data.getColumnIndexOrThrow(DbContract.RouteEntity._ID));
            String name = data.getString(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_ROUTE));
            String date = data.getString(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_DATE));
            Double distance = data.getDouble(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_DISTANCE));
            Double rating = data.getDouble(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_RATING));
            String tags = data.getString(data.getColumnIndexOrThrow(DbContract.RouteEntity.COLUMN_TAG));

            route = new RouteClass(id, name, date, distance, rating, tags);
            tempList.add(route);
            count += 1;
        }

        //log the number of items in database
        Log.d("ROUTES_DB", "There are " + count + " items in the Database");
        return tempList;
    }

    public List getAllMapData() {
        return null; //replace
    }

    public void createDummyData(){
        RouteClass route = new RouteClass(1, "Route 1", "11/11/2019", 12.05, 4.0, "");
        addItem(DbContract.RouteEntity.TABLE_NAME, route);
        route = new RouteClass(2, "Route 2", "11/02/2019", 3.45, 3.5, "Store");
        addItem(DbContract.RouteEntity.TABLE_NAME, route);
        route = new RouteClass(3, "Route 3", "10/15/2019", 6.94, 3.0, "");
        addItem(DbContract.RouteEntity.TABLE_NAME, route);
        route = new RouteClass(4, "Route 4", "9/14/2019", 4.02, 1.5, "Old");
        addItem(DbContract.RouteEntity.TABLE_NAME, route);
        route = new RouteClass(2, "Route 5", "8/29/2019", 8.29, 5.0, "Vacation/Amusement Park");
        addItem(DbContract.RouteEntity.TABLE_NAME, route);
    }
}
