package com.zachl.restock.entities.managers;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.appcompat.app.AppCompatActivity;

import com.zachl.restock.R;

import java.util.ArrayList;

public abstract class Manager {
    public static String[] resources;
    public static final String[] types = {"hs", "tp", "wb"};

    protected AppCompatActivity activity;
    protected ArrayList<ViewGroup> v;
    protected String type;
    protected final String key;

    public Manager(AppCompatActivity activity, ArrayList<ViewGroup> v, String type, int keyID) {
        this.activity = activity;
        this.v = v;
        this.type = type;
        key = activity.getString(keyID);
        resources = new String[]{activity.getString(R.string.resource_2), activity.getString(R.string.resource_1), activity.getString(R.string.resource_3)};
    }

    public void apply(){
        modify(collect(v));
    }

    public static int getTypeIndex(String type){
        for(int i = 0; i < types.length; i++){
            if(type.equalsIgnoreCase(types[i]))
                return i;
        }
        return 0;
    }

    private ArrayList<View> collect(ArrayList<ViewGroup> av) {
        ArrayList<View> views = new ArrayList<>();
        for(ViewGroup v : av) {
            for (int i = 0; i < v.getChildCount(); i++) {
                if (v.getChildAt(i).getContentDescription() != null && v.getChildAt(i).getContentDescription().toString().contains(key)) {
                    views.add(v.getChildAt(i));
                }
            }
        }
        return views;
    }

    public abstract void modify(ArrayList<View> views);
}
