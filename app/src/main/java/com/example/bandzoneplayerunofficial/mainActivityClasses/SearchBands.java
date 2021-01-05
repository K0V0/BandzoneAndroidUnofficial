package com.example.bandzoneplayerunofficial.mainActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchBands extends OnFinishTypingHelper {
    private String url = "http://172.104.155.216:3030/search/bands?q=";
    private String query = "";
    private Gson gson = new Gson();
    private JSONObject responseData;
    private int pagesCount = 1;
    private int currentPage = 1;
    private int itemsTotal;
    private int nextPageToLoad = 1;
    private List<Band> bandsList;
    private Type bandListType = new TypeToken<ArrayList<Band>>(){}.getType();
    private Activity mainActivity;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    //private Context context;

    public SearchBands(MainActivity mainActivity, Context context) {
        super();
        this.mainActivity = mainActivity;
        //this.context = context;
        this.mRecyclerView = (RecyclerView) this.mainActivity.findViewById(R.id.bandsList);
        this.layoutManager = new LinearLayoutManager(this.mainActivity);
        this.mRecyclerView.setLayoutManager(layoutManager);
        this.mRecyclerView.setHasFixedSize(true);
        afterConstruct();
    }

    @Override
    public void doStuff() {
        // only runs on write, no need to page load limit, always 1
        this.nextPageToLoad = 1;
        doJsonRequest();
    }

    private void doJsonRequest() {
        this.query = getText() + "&p=" + this.nextPageToLoad;
        //System.out.println(this.query);
        StringRequest request = new StringRequest(Request.Method.GET, this.url + this.query, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    responseData = (new JSONObject(s)).getJSONObject("table");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonParseListProperties(responseData);
                jsonParseBands(responseData);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("error getting json from server");
                // dat tu nejaku chybovu hlasku
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(mainActivity);
        rQueue.add(request);
    }

    private void jsonParseListProperties(JSONObject json) {
        try {
            this.pagesCount = json.getInt("pages_count");
            this.currentPage = json.getInt("current_page");
            this.itemsTotal = json.getInt("items_total");
            //System.out.println(pagesCount);
            //System.out.println(itemsTotal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (this.currentPage + 1 <= this.pagesCount) {
            this.nextPageToLoad = this.currentPage + 1;
        } else {
            this.nextPageToLoad = 0;
        }
    }

    private void jsonParseBands(JSONObject json) {
        JSONArray arr = new JSONArray();
        try {
            arr = json.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ((this.bandsList == null)||(this.currentPage == 1)) {
            // first load
            bandsList = gson.fromJson(String.valueOf(arr), bandListType);
            //bandsList.add(null);
            mAdapter = new RecyclerAdapter(mainActivity, bandsList);
            mRecyclerView.setAdapter(mAdapter);
            //System.out.println("first load");
        } else {
            /*bandsList.remove(bandsList.size() - 1);
            mAdapter.notifyItemRemoved(bandsList.size());*/
            bandsList.addAll(gson.fromJson(String.valueOf(arr), bandListType));
            //mAdapter.addNewPagesToList(bandsList);
            mAdapter.notifyItemInserted(bandsList.size() - 1);
            //System.out.println("next load");
        }
    }

    private void afterConstruct() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    //System.out.println("cannot scroll more");
                    if (nextPageToLoad != 0) {
                        //nextPageToLoad = 0;
                        // netrpezlivy user
                        System.out.println("loading");
                        // if loading added
                        /*bandsList.add(null);
                        mAdapter.notifyItemInserted(bandsList.size() - 1);*/
                        /*bandsList.remove(bandsList.size() - 1);
                        mAdapter.notifyItemRemoved(bandsList.size());*/
                        doJsonRequest();
                    }
                }
            }
        });
    }

}
