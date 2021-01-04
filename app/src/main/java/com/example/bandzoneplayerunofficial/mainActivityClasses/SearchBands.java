package com.example.bandzoneplayerunofficial.mainActivityClasses;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bandzoneplayerunofficial.MainActivity;
import com.example.bandzoneplayerunofficial.R;
import com.example.bandzoneplayerunofficial.helpers.OnFinishTypingHelper;
import com.example.bandzoneplayerunofficial.objects.Band;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchBands extends OnFinishTypingHelper {
    private String url = "http://172.104.155.216:3030/search/bands?q=";
    private String query = "";
    private Activity mainActivity;
    private List<Band> bandsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;

    public SearchBands(MainActivity mainActivity, Context context) {
        super();
        this.mainActivity = mainActivity;
        this.context = context;
    }

    @Override
    public void doStuff() {
        this.query = getText();
        StringRequest request = new StringRequest(Request.Method.GET, this.url+this.query, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                parseJson(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("erooooooor");
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(mainActivity);
        rQueue.add(request);
    }

    private void parseJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.getJSONObject("table").getJSONArray("data");
            Gson gson = new Gson();
            Type bandListType = new TypeToken<ArrayList<Band>>(){}.getType();
            bandsList = gson.fromJson(String.valueOf(arr), bandListType);
            mRecyclerView = (RecyclerView) this.mainActivity.findViewById(R.id.bandsList);
            layoutManager = new LinearLayoutManager(mainActivity);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mAdapter = new RecyclerAdapter(mainActivity, bandsList);
            mRecyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
