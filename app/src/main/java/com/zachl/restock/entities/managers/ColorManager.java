package com.zachl.restock.entities.managers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.zachl.restock.R;

import java.util.ArrayList;

public class ColorManager extends Manager{
    public enum Color{
        HS(R.color.hs),
        TP(R.color.tp),
        WB(R.color.wb),
        MAIN(R.color.main),
        HAZARD(R.color.hazard);

        public int val;
        Color(int val){
            this.val = val;
        }
    }
    public ColorManager(AppCompatActivity activity, ArrayList<ViewGroup> v, String type){
        super(activity, v, type, R.string.color_mod);
    }

    @Override
    public void modify(ArrayList<View> views){
        for(View view : views){
            DrawableCompat.setTint(((ImageView)view).getDrawable(), ContextCompat.getColor(activity.getApplicationContext(), Color.values()[getTypeIndex(type)].val));
        }
    }
}
