package com.zachl.restock.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zachl.restock.R;
import com.zachl.restock.entities.managers.Manager;

import java.util.ArrayList;

public class PopUp extends AppCompatActivity {
    public static final String key = "COM.ZACHL.RESTOCK.POPUP";
    public static final String type_key = "COM.ZACHL.RESTOCK.POPUP.TYPE";
    private static ArrayList<String[]> sources = new ArrayList<>();
    private static String[] keys;
    private TextView hint;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.4));

        String hkey = getIntent().getStringExtra(type_key);
        keys = getResources().getStringArray(R.array.help_keys);
        sources.add(getResources().getStringArray(R.array.resource_hints));
        sources.add(getResources().getStringArray(R.array.size_hints));
        sources.add(getResources().getStringArray(R.array.calc_hints));
        for(int i = 0; i < keys.length; i++){
            if(keys[i].equalsIgnoreCase(hkey)){
                hint = findViewById(R.id.hint);
                String type = getIntent().getStringExtra(key);
                hint.setText(sources.get(i)[Manager.getTypeIndex(type)]);
                break;
            }
        }
    }
}
