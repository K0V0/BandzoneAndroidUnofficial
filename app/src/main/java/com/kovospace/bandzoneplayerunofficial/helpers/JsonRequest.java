package com.kovospace.bandzoneplayerunofficial.helpers;

import android.app.Activity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kovospace.bandzoneplayerunofficial.R;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonRequest {
    protected Activity activity;
    protected String query;
    protected JSONObject responseData;
    protected ToastMessage toastMessage;

    public JsonRequest(Activity activity) {
        this(activity, "");
    }
    public JsonRequest(Activity activity, String query) {
        this.query = query;
        this.activity = activity;
        toastMessage = new ToastMessage(activity);
    }

    public abstract void doStuff();

    public void setQuery(String query) {
        this.query = query;
    }

    public void fetch() {
        requestData();
    }

    public void fetch(String query) {
        setQuery(query);
        requestData();
    }

    private void validateQuery() {
        this.query = this.query.replace(" ", "%20");
    }

    private void requestData() {
        validateQuery();
        StringRequest request = new StringRequest(Request.Method.GET, this.query, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    responseData = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                doStuff();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toastMessage.send(R.string.noInternet);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }
}
