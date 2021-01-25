package com.example.fb_application;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

public class OptionActivity extends AppCompatActivity {

    private static final String TAG = "OptionActivity" ;
    EditText ip ;
    Switch dark ;
    Spinner lng ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MySingletonClass.getInstance().isDarkMode()){
            getTheme().applyStyle(R.style.AppThemeDark, true);
        }
        else{
            getTheme().applyStyle(R.style.AppThemeLight, true);
        }

        setContentView(R.layout.activity_option);
        Toolbar optionToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(optionToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.Parameters);
        ab.setDisplayHomeAsUpEnabled(true);

        ip = (EditText) findViewById(R.id.editTextAddressPi);
        dark = (Switch) findViewById(R.id.DarkMode);
        lng = (Spinner) findViewById(R.id.LanguageArray);

        ip.setText(MySingletonClass.getInstance().getIpAddress());
        dark.setChecked(MySingletonClass.getInstance().isDarkMode());
        lng.setSelection(MySingletonClass.getInstance().getLanguage());
    }


    public void Validate(View view) {

        Log.e(TAG, "Validate");
        Boolean check = dark.isChecked();

        int index = lng.getSelectedItemPosition();

        MySingletonClass.getInstance().setIpAddress(String.valueOf(ip.getText()));
        MySingletonClass.getInstance().setDarkMode(check);
        MySingletonClass.getInstance().setLanguage(index);

        //changement de la langue
        Locale locale =  null;
        if (index ==0){
            locale = new Locale("en");
        } else  if (index ==1){
            locale = new Locale("en");
        } else  if (index ==2){
            locale = new Locale("en");
        }

        Locale.setDefault(locale);
        Context context = getApplicationContext();
        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);
        context = context.createConfigurationContext(configuration);

        if (index ==0){
            setApplicationLocale("fr");
        }else if (index ==1){
            setApplicationLocale("en");
        }else if (index ==2){
            setApplicationLocale("it");
        }


        //context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, R.string.ToastValidate, duration);
        toast.show();
    }

    private void setApplicationLocale(String locale) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(locale.toLowerCase()));
        } else {
            config.locale = new Locale(locale.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }
}