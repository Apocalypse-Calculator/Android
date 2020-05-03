package com.zachl.restock.entities.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpRequest {

    final String url = "https://condescending-neumann-62a029.netlify.app/.netlify/functions/users/register";
    AppCompatActivity activity;

    public SignUpRequest(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void register(String email, String password, String passwordConfirm, String firstName, String lastName, String displayName) {
        this.register(email, password, passwordConfirm, firstName, lastName, displayName, null);
    }

    public void register(String email, String password, String passwordConfirm, String firstName, String lastName, String displayName, String location) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity);

        try {
            //Creating the request's body
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("password_confirm", passwordConfirm);
            jsonBody.put("first_name", firstName);
            jsonBody.put("last_name", lastName);
            jsonBody.put("display_name", displayName);
            if(location != null) jsonBody.put("location", new JSONObject(location)); //as location is optional, according to the API docs
            final String requestBody = jsonBody.toString();

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //success
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error
                        try {
                            //getting error messages
                            String body = new String(error.networkResponse.data,"UTF-8");
                            JSONObject errorJson = new JSONObject(body);
                            JSONArray errorsJsonArray = errorJson.getJSONArray("errors");

                            List<String> errorsList = new ArrayList<String>();
                            for(int i = 0; i < errorsJsonArray.length(); i++){
                                errorsList.add(errorsJsonArray.getString(i));
                            }
                        } catch (Exception e) {
                            //error
                            e.printStackTrace();
                        }
                    }
                }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };


            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (Exception e) {
            //error
            e.printStackTrace();
        }

    }
}