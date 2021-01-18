package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bandzoneplayerunofficial.R;
import com.example.bandzoneplayerunofficial.helpers.Misc;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadBand {
    private Misc misc;
    private final String QUERY_URL = "http://172.104.155.216:3030/band/";
    private Activity activity;
    private Context context;
    private String query;
    private JSONObject responseData;

    public LoadBand() {}

    public LoadBand(Activity activity, Context context) {
        this();
        this.activity = activity;
        this.context = context;
        this.misc = new Misc(this.context);
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void load() {
        doJsonRequest();
    }

    private void doJsonRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, this.QUERY_URL + query, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    responseData = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                renderBandInfo();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                misc.toastMessage(R.string.noInternet);
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }

    private void renderBandInfo() {

    }

}
