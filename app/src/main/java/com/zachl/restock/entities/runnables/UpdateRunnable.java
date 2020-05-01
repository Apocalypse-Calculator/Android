package com.zachl.restock.entities.runnables;

import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateRunnable {
    public interface Updater{
        void update(View view);
    }

    private Updater updater;
    private Timer timer;
    public int refresh;
    private boolean running;

    public UpdateRunnable(Updater updater){
        timer = new Timer();
        this.refresh = 5;
        this.updater = updater;
    }

    public UpdateRunnable(Updater updater, int refresh){
        timer = new Timer();
        this.refresh = refresh;
        this.updater = updater;
    }
    public void start(View view) {
        final View fView = view;
        running = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    updater.update(fView);
                }
            }, 0, refresh);
    }
    public void end(){
        timer.cancel();
        running = false;
    }

    public boolean running(){
        return running;
    }
}

