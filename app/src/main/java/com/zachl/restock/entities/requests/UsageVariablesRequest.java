package com.zachl.restock.entities.requests;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class UsageVariablesRequest {

    final String url = "https://condescending-neumann-62a029.netlify.app/.netlify/functions/item-definitions";
    AppCompatActivity activity;

    public UsageVariablesRequest(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void getVariables() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Saving received json data in shared preferences
                    SharedPreferences sharedPref = activity.getApplicationContext().getSharedPreferences("restock", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        JSONArray definitions = responseJSON.getJSONArray("definitions");
                        for (int i = 0; i < definitions.length(); i++) {
                            JSONObject definition = definitions.getJSONObject(i);
                            JSONObject obj = new JSONObject();
                            obj.put("unit", definition.getString("unit"));
                            obj.put("averageConsumption", definition.getDouble("averageConsumption"));
                            editor.putString(definition.getString("name"), obj.toString());
                        }
                        editor.apply();
                    } catch (JSONException err){
                        //error
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //error
                }
            }
        );

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
