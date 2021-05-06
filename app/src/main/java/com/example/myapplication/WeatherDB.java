package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDB extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "weather.db";

    private static WeatherDB theDb;

    private WeatherDB(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static synchronized WeatherDB getInstance(Context context) {
        if (theDb == null) {
            // Make sure that we do not leak Activity's context
            theDb = new WeatherDB(context.getApplicationContext());
        }

        return theDb;
    }
    private static final String[] SQL_CREATE_TABLES = {
            "CREATE TABLE weather (" +
                    "city TEXT PRIMARY KEY," +
                    "weather TEXT," +
                    "temperature REAL, " +
                    "humidity INTEGER, " +
                    "wind REAL)"
    };
    private static final String[] SQL_DELETE_TABLES = {
            "DROP TABLE IF EXISTS weather"
    };
    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String command: SQL_CREATE_TABLES) {
            db.execSQL(command);
        }
        ContentValues values = new ContentValues();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String command: SQL_DELETE_TABLES) {
            db.execSQL(command);
        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
