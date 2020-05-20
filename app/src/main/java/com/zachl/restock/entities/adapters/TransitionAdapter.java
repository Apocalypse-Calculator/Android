package com.zachl.restock.entities.adapters;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class TransitionAdapter {
    AppCompatActivity act;
    public TransitionAdapter(AppCompatActivity act){
        this.act = act;
    }
    public void changeLayout(ConstraintLayout layout, long length, int id){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.load(act, id);
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(length);
        TransitionManager.beginDelayedTransition(layout, autoTransition);
        constraintSet.applyTo(layout);
    }
    public void changeLayout(ConstraintLayout layout, int id){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.load(act, id);
        TransitionManager.beginDelayedTransition(layout);
        constraintSet.applyTo(layout);
    }
    public void fade(int id, float from, float to, long length){
        final ImageView img = act.findViewById(id);
        Animation fadeOut = new AlphaAnimation(from, to);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(length);
        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation){}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });
        img.startAnimation(fadeOut);
    }

    public void fadeAll(int[] ids, float from, float to, long length) {
        for (int id : ids) {
            fade(id, from, to, length);
        }
    }

    public void fadeAll(int[] ids, float from, float to, long length, long interLength) {
        for(int i = 0; i < ids.length; i++){
            fade(ids[i], from, to, length + (interLength * i));
        }
    }
}
