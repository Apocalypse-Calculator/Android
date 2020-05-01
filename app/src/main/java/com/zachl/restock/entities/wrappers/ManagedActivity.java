package com.zachl.restock.entities.wrappers;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.zachl.restock.entities.managers.ColorManager;
import com.zachl.restock.entities.managers.HazardManager;
import com.zachl.restock.entities.managers.Manager;
import com.zachl.restock.entities.managers.ViewManager;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class ManagedActivity extends AppCompatActivity {
    protected ViewManager vm ;
    protected ColorManager cm;
    protected String type = "hs";
    private Manager[] managers;
    protected FirebaseAnalytics firebaseAnalytics;
    protected Intent intent;

    public enum Package{
        Default(new Class[]{ViewManager.class, ColorManager.class}),
        All(new Class[]{ViewManager.class, ColorManager.class, HazardManager.class});

        Class[] contents;
        Package(Class[] contents){
            this.contents = contents;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        adjustFontScale(getResources().getConfiguration());
        //analyticsInit();
    }
    private void analyticsInit(){
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void adjustFontScale(Configuration configuration) {
        if (configuration.fontScale > 1.2) {
            configuration.fontScale = (float) 1;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getBaseContext().getResources().updateConfiguration(configuration, metrics);
        }
    }
    protected void setNextButton(ImageButton button, Class type, final String[] keys, final String[] values){
        intent = new Intent(getApplicationContext(), type);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < keys.length; i++){
                    intent.putExtra(keys[i], values[i]);
                }
                startActivity(intent);
            }
        });
    }
    protected void build(ArrayList<ViewGroup> v, String type, Package pack){
        for(int i = 0; i < pack.contents.length; i++){
            try {
                Constructor<?> ctor = pack.contents[i].getConstructor(AppCompatActivity.class, ArrayList.class, String.class);
                Object object = ctor.newInstance(this, v, type);
                ((Manager)object).apply();
            }
            catch(Exception e){
                //e.printStackTrace();
            }
        }
    }
    protected void buildManager(ArrayList<ViewGroup> v, String type, Class manager, double result){
        try {
            Constructor<?> ctor = manager.getConstructor(AppCompatActivity.class, ArrayList.class, String.class);
            Object object = ctor.newInstance(this, v, type);
            ((HazardManager) object).setPositive(result);
            ((Manager)object).apply();
        }
        catch(Exception e){
            //e.printStackTrace();
        }
    }
}
