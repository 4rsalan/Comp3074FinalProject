package ca.georgebrown.comp3074.mymovement;

import android.provider.BaseColumns;

public final class DbContract {
    public DbContract(){}

    public class RouteEntity implements BaseColumns {
        public static final String TABLE_NAME = "routes";
        public static final String COLUMN_ROUTE = "route_name";
        public static final String COLUMN_DATE = "date_created";
        public static final String COLUMN_DISTANCE = "distance_walked";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_TAG = "tags";

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( "
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_ROUTE + " TEXT, "
                + COLUMN_DATE + " TEXT,"
                + COLUMN_DISTANCE + " DOUBLE(6, 2), "
                + COLUMN_RATING + " DOUBLE(2, 1), "
                + COLUMN_TAG + " TEXT "
                + ")";
        public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public class MapDataEntity implements BaseColumns {
        public static final String TABLE_NAME = "map_data";
        public static final String COLUMN_ROUTE_ID = "route_id";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_ALTITIUDE = "altitude";
        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( "
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_ROUTE_ID + " INTEGER NOT NULL, "
                + COLUMN_LATITUDE + " DOUBLE, "
                + COLUMN_LONGITUDE + " DOUBLE, "
                + COLUMN_ALTITIUDE + " DOUBLE, "
                + COLUMN_TIMESTAMP + " DOUBLE "
                + ")";

        public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
