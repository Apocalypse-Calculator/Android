package com.zachl.restock.entities.adapters;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.zachl.restock.entities.managers.Manager;

public class FirebaseAdapter {
    private FirebaseAnalytics firebaseAnalytics;
    public FirebaseAdapter(Activity activity){
        firebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    }
    public void logContent(String key){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, key);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Manager.resources[Manager.getTypeIndex(key)]);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Item");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle);
    }
    public void logHelp(String key, int pos){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, key + pos);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Manager.resources[Manager.getTypeIndex(key)] + " Help " + pos);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Help Pop-Up");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, bundle);
    }
}
