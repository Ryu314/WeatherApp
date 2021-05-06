package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText e;
    Button b;
    SharedPreferences prefs;
    public static final String MyPREFERENCES = "WeatherPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (prefs.getString("Name", null) !=null) {
            Intent i = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(i);
        } else {
            e = findViewById(R.id.editTextTextPersonName);
            b = findViewById(R.id.button3);
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = e.getText().toString();
                if (a.length() == 0) {
                    Toast.makeText(MainActivity.this, "Enter a city", Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("City", a);
                    editor.commit();
                    Toast.makeText(MainActivity.this, "City Stored", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this, WeatherActivity.class);
                    startActivity(i);
                }
            }

        });
    }
}