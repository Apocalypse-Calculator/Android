package com.zachl.restock.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zachl.restock.R;
import com.zachl.restock.entities.managers.ButtonManager;
import com.zachl.restock.entities.requests.UsageVariablesRequest;
import com.zachl.restock.entities.runnables.BufferRunnable;
import com.zachl.restock.entities.runnables.UpdateRunnable;
import com.zachl.restock.entities.wrappers.ExpandingButton;
import com.zachl.restock.entities.wrappers.ManagedActivity;

import java.util.ArrayList;

public class MainActivity extends ManagedActivity {
    /**
     * Every activity is a screen/page on the app
     */
    public static final String EXTRA = "COM.ZACHL.CALCULATOR.TYPE";
    private TextView descT;
    private ConstraintLayout ui;
    private int descI;
    private String[] descSrces;
    private int headerBuffer = 10;
    private boolean initd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_key1);
        ArrayList<ViewGroup> roots = new ArrayList<>();
        roots.add((ViewGroup)findViewById(R.id.tp_button).getParent());
        buildManager(roots, "", ButtonManager.class, 0);
        ui = findViewById(R.id.ui);

        BufferRunnable buffer = new BufferRunnable(new BufferRunnable.Buffer() {
            @Override
            public void wake() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        transitionAdapter.changeLayout(ui, R.layout.activity_main);
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
    @Override
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
                    String key = view.getContentDescription().toString().replace(" " + getString(R.string.button_mod), "");
                    intent.putExtra(EXTRA, key);
                    firebaseAdapter.logContent(key);
                    startActivity(intent);
                    initd = true;
                }
        }
    }
}
