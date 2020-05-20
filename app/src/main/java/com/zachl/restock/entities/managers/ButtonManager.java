package com.zachl.restock.entities.managers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.zachl.restock.R;
import com.zachl.restock.entities.wrappers.ExpandingButton;
import com.zachl.restock.entities.wrappers.ManagedActivity;

import java.util.ArrayList;

public class ButtonManager extends Manager {
    public ButtonManager(AppCompatActivity activity, ArrayList<ViewGroup> v, String type) {
        super(activity, v, type, R.string.button_mod);
    }

    @Override
    public void modify(ArrayList<View> views) {
        for(View view : views){
            ExpandingButton button = new ExpandingButton(activity.getApplicationContext(), (ImageButton) view, new ExpandingButton.Triggerable() {
                @Override
                public void trigger(View view) {
                    ((ManagedActivity)activity).trigger(view);
                }
            });
        }
    }
}
