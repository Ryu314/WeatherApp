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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    EditText e;
    Button b;
    SharedPreferences prefs;
    public static final String UnitPREFERENCES = "UnitPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences(UnitPREFERENCES, Context.MODE_PRIVATE);
        e = findViewById(R.id.cityEditText);
        b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = e.getText().toString();
                if (a.length() == 0) {
//                    Toast.makeText(MainActivity.this, "Name length cant be less than 0", Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("Unit", a);
                    editor.commit();
                    Toast.makeText(SettingActivity.this, "Unit Stored", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SettingActivity.this, WeatherActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}