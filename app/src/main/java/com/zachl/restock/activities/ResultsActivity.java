package com.zachl.restock.activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zachl.restock.R;
import com.zachl.restock.entities.managers.HazardManager;
import com.zachl.restock.entities.managers.Manager;
import com.zachl.restock.entities.math.Function;
import com.zachl.restock.entities.runnables.BufferRunnable;
import com.zachl.restock.entities.runnables.UpdateRunnable;
import com.zachl.restock.entities.wrappers.ManagedActivity;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends ManagedActivity{
    private int[] answers = new int[6];
    private String type;
    private Function.Multiplier eq;
    private double[] results;
    private UpdateRunnable updateR;
    private View[] calcs = new View[3];
    private ConstraintLayout ui;
    private int resultI = 0;
    private int headerBuffer = 5;
    private int textBuffer = 8;
    private View icon;
    BufferRunnable buffer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_key1);
        /**
         * RESTPOINT 2 - PUSH
         * - This requires all of the users entered data AND location - which we don't have set up yet
         */
        Intent intent = getIntent();
        type = intent.getStringExtra(MainActivity.EXTRA);
        icon = findViewById(R.id.icon2);
        ArrayList<ViewGroup> av = new ArrayList<>();
        av.add((ViewGroup)icon.getParent());
        av.add((ViewGroup)findViewById(R.id.textLayout));
        build(av, type, Package.Default);
        for(int i = 0; i < answers.length - 1; i++){
            answers[i] = Integer.valueOf(intent.getStringExtra(MainActivity.EXTRA + CalculatorActivity.EXTRA_SUFF + i));
        }
        answers[answers.length - 1] = (Float.valueOf(intent.getStringExtra(MainActivity.EXTRA + CalculatorActivity.EXTRA_SIZE))).intValue();

        switch(type){
            case "hs":
                eq = Function.Multiplier.HS;
                break;
            case "tp":
                eq = Function.Multiplier.TP;
                break;
            case "wb":
                eq = Function.Multiplier.WB;
                break;
        }
        Function function = new Function(answers);
        results = function.apply(eq);
        buildManager(av, type, HazardManager.class, results[1]);
        results[1] = Math.abs(results[1]);

        calcs[0] = findViewById(R.id.results1);
        calcs[1] = findViewById(R.id.results2);
        ui = findViewById(R.id.menu);
        BufferRunnable buffer = new BufferRunnable(new BufferRunnable.Buffer() {
            @Override
            public void wake() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        start();
                    }
                });
            }
        }, headerBuffer);
        buffer.start();

       buffer2 = new BufferRunnable(new BufferRunnable.Buffer() {
            @Override
            public void wake() {
                textChange();
            }
        }, textBuffer);
        buffer2.start();
    }

    public void goHome(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    public void start(){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.load(this, R.layout.activity_results);
        TransitionManager.beginDelayedTransition(ui);
        constraintSet.applyTo(ui);
    }
    public void textChange(){
        buffer2.end();
        updateR = new UpdateRunnable(new UpdateRunnable.Updater() {
            @Override
            public void update(View view) {
                if(Integer.valueOf(((TextView)view).getText().toString()) < results[resultI]){
                    final View fview = view;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView)fview).setText(Integer.toString(Integer.valueOf(((TextView)fview).getText().toString()) + 1));
                        }
                    });
                }
                else{
                    updateR.end();
                    ((TextView)view).setText("" + (int)results[resultI]);
                    if(resultI < 1) {
                        resultI++;
                        textChange();
                    }
                }
            }
        }, 50);
        updateR.start(calcs[resultI]);
    }

    public void openAmazon() {
        String itemName = Manager.resources[Manager.getTypeIndex(type)];
        String[] tokens = itemName.split(" ");
        String plusSeparated = tokens[0];
        for(int i = 1; i < tokens.length; i++) {
            plusSeparated +="+" + tokens[i];
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(R.string.amazon_url + plusSeparated));
        startActivity(browserIntent);
    }

    public void openGMaps() {
        Uri gmmIntentUri = Uri.parse(getString(R.string.gmaps_stores_uri));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage(getString(R.string.gmaps_package));
        startActivity(mapIntent);
    }
}
