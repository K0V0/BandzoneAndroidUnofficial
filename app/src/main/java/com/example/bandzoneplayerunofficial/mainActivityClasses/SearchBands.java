package com.example.bandzoneplayerunofficial.mainActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;
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

    private final String QUERY_URL = "http://172.104.155.216:3030/search/bands?q=";
    private final int ITEMS_PER_PAGE = 20;

    private String query = "";
    private Gson gson = new Gson();
    private JSONObject responseData;
    private int pagesCount = 1;
    private int currentPage = 1;
    private int itemsTotal;
    private int nextPageToLoad = 1;
    private int itemsOnViewport;
    private int itemsOnActualPage;
    private List<Band> bandsList = new ArrayList<>();
    private Type bandListType = new TypeToken<ArrayList<Band>>(){}.getType();
    private Activity mainActivity;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private boolean stopMultipleLoads = false;

    public SearchBands(MainActivity mainActivity, Context context) {
        super();
        this.mainActivity = mainActivity;
        this.context = context;
        this.mRecyclerView = (RecyclerView) this.mainActivity.findViewById(R.id.bandsList);
        this.layoutManager = new LinearLayoutManager(this.mainActivity);
        this.mRecyclerView.setLayoutManager(layoutManager);
        this.mRecyclerView.setHasFixedSize(true);
        this.mAdapter = new RecyclerAdapter(this.context, bandsList);
        this.mRecyclerView.setAdapter(mAdapter);
        afterConstruct();
        doJsonRequest(); // let bandzone to load random bands on first run
    }

    @Override
    public void doStuff() {
        // only runs on write, no need to page load limit, always 1
        this.nextPageToLoad = 1;
        doJsonRequest();

        // let bandzone choose some bands when searchfield deleted
        if ((this.getText().length() == 0)&&(this.bandsList.size() > 0)) {
            this.bandsList.clear();
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void doJsonRequest() {
        this.query = getText() + "&p=" + this.nextPageToLoad;
        StringRequest request = new StringRequest(Request.Method.GET, this.QUERY_URL + this.query, new Response.Listener<String>() {
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
                // dat tu nejaku chybovu hlasku "neni internet"
                noInternetMessage();
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
            this.itemsOnActualPage = json.getInt("items_current_page");
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
            this.bandsList.clear();
            this.bandsList.addAll(gson.fromJson(String.valueOf(arr), this.bandListType));
            this.mAdapter.notifyDataSetChanged();
            layoutManager.smoothScrollToPosition(mRecyclerView, new RecyclerView.State(),0);
        } else {
            // next pages
            removeLoadingDialog();
            bandsList.addAll(gson.fromJson(String.valueOf(arr), bandListType));
            mAdapter.notifyItemInserted(bandsList.size() - 1);
            layoutManager.smoothScrollToPosition(
                    mRecyclerView,
                    new RecyclerView.State(),
                    calculateScroll(mRecyclerView)
            );
        }
        stopMultipleLoads = false;
    }

    private void afterConstruct() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    if (nextPageToLoad != 0) {
                        if (!stopMultipleLoads) {
                            stopMultipleLoads = true;
                            displayLoadingDialog();
                            doJsonRequest();
                        }
                    }
                }
            }
        });
    }

    private void displayLoadingDialog() {
        this.bandsList.add(null);
        this.mAdapter.notifyItemInserted(this.bandsList.size() - 1);
    }

    private void removeLoadingDialog() {
        for (int i = 0; i < this.bandsList.size(); i++) {
            if (this.bandsList.get(i) == null) {
                this.bandsList.remove(i);
                this.mAdapter.notifyItemRemoved(i);
            }
        }
    }

    private int calculateScroll(RecyclerView rv) {
        this.itemsOnViewport = getVisibleItemCount(mRecyclerView);
        return bandsList.size() - (this.itemsOnActualPage - this.itemsOnViewport+2);
    }

    private static int getVisibleItemCount(RecyclerView rv) {
        final int firstVisiblePos = getFirstVisiblePosition(rv);
        final int lastVisiblePos = getLastVisiblePosition(rv);
        return Math.max(0, lastVisiblePos - firstVisiblePos);
    }

    private static int getFirstVisiblePosition(RecyclerView rv) {
        if (rv != null) {
            final RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            }
        }
        return 0;
    }

    private static int getLastVisiblePosition(RecyclerView rv) {
        if (rv != null) {
            final RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
        }
        return 0;
    }

    private void noInternetMessage() {
        Toast toast = Toast.makeText(this.context, R.string.noInternet, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
