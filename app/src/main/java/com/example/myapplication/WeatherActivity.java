package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    String city;
    String unit = "Imperial";
    String tempUnit;
    String windUnit;
    SharedPreferences WeatherPrefs;
    SharedPreferences UnitPrefs;
    public static final String WeatherPREFERENCES = "WeatherPrefs";
    public static final String UnitPREFERENCES = "UnitPrefs";
    SQLiteDatabase theDB;
    TextView aCity;
    TextView w;
    TextView t;
    TextView h;
    TextView w2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WeatherPrefs = getSharedPreferences(WeatherPREFERENCES, Context.MODE_PRIVATE);
        UnitPrefs = getSharedPreferences(UnitPREFERENCES, Context.MODE_PRIVATE);
        city = WeatherPrefs.getString("City", null);
        aCity = (TextView)findViewById(R.id.city);
        w = (TextView)findViewById(R.id.weather);
        t = (TextView)findViewById(R.id.temperature);
        h = (TextView)findViewById(R.id.humidity);
        w2 = (TextView)findViewById(R.id.wind);
        if(UnitPrefs.getString("Unit", null) != null){
            unit = UnitPrefs.getString("Unit", null);
        }
        if(unit.equalsIgnoreCase("Imperial")){
            windUnit = " miles/hr";
            tempUnit = " F";
        }
        else if(unit.equalsIgnoreCase("Metric")){
            windUnit = " m/s";
            tempUnit = " C";
        }
        else{
            windUnit = " m/s";
            tempUnit = " K";
        }
         new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... args) {
                theDB = WeatherDB.getInstance(WeatherActivity.this).getWritableDatabase();
                return null;
            }
        }.execute();
        theDB = WeatherDB.getInstance(WeatherActivity.this).getWritableDatabase();
        getSiteData(city);

        setViews();
        Button b = findViewById(R.id.settingsButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WeatherActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });
    }

    private void setViews(){
        Cursor c = theDB.query("weather", new String[]{"*"}, "city = ?", new String[]{city}, null, null, null);
        TextView aCity = (TextView)findViewById(R.id.city);
        TextView weather = (TextView)findViewById(R.id.weather);
        TextView temperature = (TextView)findViewById(R.id.temperature);
        TextView humidity = (TextView)findViewById(R.id.humidity);
        TextView wind = (TextView)findViewById(R.id.wind);
        Log.e(TAG, "Weather column index: " + c.getColumnIndex("weather"));
        Log.e(TAG, "Temperature column index: " + c.getColumnIndex("temperature"));
        Log.e(TAG, "Humidity column index: " + c.getColumnIndex("humidity"));
        Log.e(TAG, "Wind column index: " + c.getColumnIndex("wind"));

        Log.e(TAG, "Weather column info: " + c.getString(1));
        Log.e(TAG, "Temperature column info: " + c.getDouble(2));
        Log.e(TAG, "Humidity column info: " + c.getInt(3));
        Log.e(TAG, "Wind column info: " + c.getDouble(4));
        String weatherVal = c.getString(c.getColumnIndex("weather"));
        double tempVal = c.getDouble(c.getColumnIndex("temperature"));
        String tempString = tempVal + tempUnit;
        int humVal = c.getInt(c.getColumnIndex("humidity"));
        String humString = humVal + " %";
        double windVal = c.getDouble(c.getColumnIndex("wind"));
        String windString = windVal + windUnit;
        aCity.setText(city);
        weather.setText(weatherVal);
        temperature.setText(tempString);
        humidity.setText(humString);
        wind.setText(windString);

    }


    private void getSiteData(String cityCode) {
        JSONviaHTTP.QueryStringParams params = new JSONviaHTTP.QueryStringParams();
        params.add("format", "json");
        params.add("q", cityCode);
        params.add("appid", "c2eaa9895471b2e83cc6e4af45191399");
        params.add("units", unit);

        new AsyncTask<JSONviaHTTP.QueryStringParams, Void, JSONObject>() {
            public JSONObject doInBackground(JSONviaHTTP.QueryStringParams... params) {
                return JSONviaHTTP.get("GET", "https://api.openweathermap.org/data/2.5/weather", params[0], "");
            }
            public void onPostExecute(JSONObject result) {
                try {
                    Log.e(TAG, "Before Execute");
                    String weather = result.getJSONArray("weather").getJSONObject(0).getString("description");
                    Log.e(TAG, weather);
                    double temperature = Double.valueOf(result.getJSONObject("main").getString("temp"));
                    Log.e(TAG, result.getJSONObject("main").getString("temp"));
                    int humidity = Integer.valueOf(result.getJSONObject("main").getString("humidity"));
                    Log.e(TAG, result.getJSONObject("main").getString("humidity"));
                    double wind = Double.valueOf(result.getJSONObject("wind").getString("speed"));
                    Log.e(TAG, result.getJSONObject("wind").getString("speed"));
                    Log.e(TAG, "Before Cursor");

                    /*String tempString = temperature + tempUnit;
                    String humString = humidity + " %";;
                    String windString = wind + windUnit;
                    aCity.setText(city);
                    w.setText(weather);
                    t.setText(tempString);
                    h.setText(humString);
                    w2.setText(windString);
                    */
                    Log.e(TAG, "Get Values");
                    ContentValues values = new ContentValues();
                    values.put("city", city);
                    values.put("weather", weather);
                    values.put("temperature", temperature);
                    values.put("humidity", humidity);
                    values.put("wind", wind);


                    Log.e(TAG, "Before Cursor");
                    theDB.insert("weather", null, values);
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        }.execute(params);
    }
}
