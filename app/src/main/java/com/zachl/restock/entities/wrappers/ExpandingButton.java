package com.zachl.restock.entities.wrappers;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;


import androidx.appcompat.widget.AppCompatImageButton;

import com.zachl.restock.entities.runnables.BufferRunnable;
import com.zachl.restock.entities.runnables.UpdateRunnable;

public class ExpandingButton extends AppCompatImageButton {
    private OnTouchListener expandL;
    private boolean triggered, initd;
    private UpdateRunnable updateR;
    public BufferRunnable bufferR;
    private Triggerable triggerable;
    private float tempScale;
    public interface Triggerable{
        void trigger(final View view);
    }

    public ExpandingButton(Context context, ImageButton button, Triggerable triggerable) {
        super(context);
        this.triggerable = triggerable;
        expandL = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && !triggered) {
                    expand(v, 1.3f, 0.015f);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || triggered){
                    expand(v, 1f, -0.01f);
                    if(event.getAction() == MotionEvent.ACTION_UP)
                        initd = false;
                    return true;
                }
                return false;
            }
        };
        button.setOnTouchListener(expandL);
    }
    public void expand(View view, final float target, final float scaleIncr){
        if(updateR != null && updateR.running()){
            updateR.end();
        }
        final View fview = view;
        if(bufferR == null) {
            bufferR = new BufferRunnable(new BufferRunnable.Buffer() {
                @Override
                public void wake() {
                    triggerable.trigger(fview);
                    bufferR.end();
                    bufferR = null;
                }
            }, 1);
            if(!initd)
                bufferR.start();
            else{
                bufferR = null;
            }
        }
        updateR = new UpdateRunnable(new UpdateRunnable.Updater() {
            @Override
            public void update(View view) {
                if (tempScale > target + 0.02f || tempScale < target -0.02f) {
                    tempScale += scaleIncr;
                    view.setScaleY(tempScale);
                    view.setScaleX(tempScale);
                } else {
                    updateR.end();
                    if(target <= 1){
                        triggered = false;
                        initd = false;
                    }
                }
            }
        });
        updateR.start(view);
        this.tempScale = view.getScaleY();
    }
    public void end() {
        bufferR.end();
        bufferR = null;
    }
}
