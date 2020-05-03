package com.zachl.restock.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zachl.restock.R;
import com.zachl.restock.entities.requests.UsageVariablesRequest;
import com.zachl.restock.entities.runnables.BufferRunnable;
import com.zachl.restock.entities.runnables.UpdateRunnable;
import com.zachl.restock.entities.wrappers.ExpandingButton;

public class MainActivity extends AppCompatActivity{
    /**
     * Every activity is a screen/page on the app
     */
    public static final String EXTRA = "COM.ZACHL.CALCULATOR.TYPE";
    private UpdateRunnable updateR;
    private ImageButton tp, hs, wb;
    private TextView descT;
    private ConstraintLayout ui;

    private float scaleIncr;
    private float target;
    private int descI;
    private String[] descSrces;
    private int headerBuffer = 10;
    private float tempScale = 1;

    private boolean initd = false;
    private boolean triggered = false;
    private BufferRunnable bufferR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_key1);
        tp = findViewById(R.id.tp_button);
        hs = findViewById(R.id.hs_button);
        wb = findViewById(R.id.wb_button);
        View.OnTouchListener buttonL = new View.OnTouchListener() {
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
        tp.setOnTouchListener(buttonL);
        hs.setOnTouchListener(buttonL);
        wb.setOnTouchListener(buttonL);

        /*ExpandingButton tpB = new ExpandingButton(getApplicationContext(), tp, new ExpandingButton.Triggerable() {
            @Override
            public void trigger(View view) {
                switch(view.getId()){
                    case R.id.header:
                        if(descI == descSrces.length - 1)
                            descI = -1;
                        descI++;
                        descT.setText(descSrces[descI]);
                        break;
                    default:
                        if(!initd) {
                            Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
                            intent.putExtra(EXTRA, view.getContentDescription());
                            startActivity(intent);
                            bufferR.end();
                            bufferR = null;
                            initd = true;
                        }
                }
            }
        });*/
        ui = findViewById(R.id.ui);

        BufferRunnable buffer = new BufferRunnable(new BufferRunnable.Buffer() {
            @Override
            public void wake() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init();
                    }
                });
            }
        }, headerBuffer);
        buffer.start();

        UsageVariablesRequest usageVariablesRequest = new UsageVariablesRequest(this);
        usageVariablesRequest.getVariables();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initd = false;
    }

    public void init(){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.load(this, R.layout.activity_main);
        TransitionManager.beginDelayedTransition(ui);
        constraintSet.applyTo(ui);
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
                    trigger(fview);
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
        this.scaleIncr = scaleIncr;
        this.target = target;
        this.tempScale = view.getScaleY();
    }
    public void trigger(View view){
        switch(view.getId()){
            case R.id.header:
                if(descI == descSrces.length - 1)
                    descI = -1;
                descI++;
                descT.setText(descSrces[descI]);
                break;
            default:
                if(!initd) {
                    Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
                    intent.putExtra(EXTRA, view.getContentDescription());
                    startActivity(intent);
                    bufferR.end();
                    bufferR = null;
                    initd = true;
                }
        }
    }
}
