package com.example.fb_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //changement de themes
        if (MySingletonClass.getInstance().isDarkMode()){
            getTheme().applyStyle(R.style.AppThemeDark, true);
        }
        else{
            getTheme().applyStyle(R.style.AppThemeLight, true);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //action si click sur le rouage dans la toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_parameters) {
            Intent optionsIntent = new Intent(MainActivity.this, OptionActivity.class);
            startActivity(optionsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //pour afficher la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void buttonGotoTemp(View view) {
        Intent TemperatureIntent = new Intent(MainActivity.this, TemperatureActivity.class);
        startActivity(TemperatureIntent);
    }

    public void buttonGotoPicam(View view) {
        Intent PicamIntent = new Intent(MainActivity.this, PicamActivity.class);
        startActivity(PicamIntent);
    }

}
