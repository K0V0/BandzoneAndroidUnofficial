package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kovospace.bandzoneplayerunofficial.BandsActivity;
import com.kovospace.bandzoneplayerunofficial.helpers.Connection;
import com.kovospace.bandzoneplayerunofficial.helpers.JsonRequest;
import com.kovospace.bandzoneplayerunofficial.helpers.SearchFieldProgress;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Page;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BandsWrapperNet extends BandsWrapper {
    private final String QUERY_URL = "http://172.104.155.216:4000/bandzone/bands?q=";
    private BandsJsonRequest bandsJsonRequest;
    private String query;
    private Page page;
    private Page offlinePage;
    private Connection connectionTester;
    private BandsWrapperOffline bandsWrapperOffline;

    public BandsWrapperNet(BandsActivity bandsActivity, Context context, Connection testConnection) {
        super(bandsActivity, context);
        connectionTester = testConnection;
        bandsJsonRequest = new BandsJsonRequest(activity);
        bandsWrapperOffline = new BandsWrapperOffline();
        SearchFieldProgress.init(this.context);
    }

    public class BandsJsonRequest extends JsonRequest {
        private Gson gson = new Gson();
        private JSONArray bandsJsonArrray = new JSONArray();
        private List<Band> bandsList = new ArrayList<>();
        private Type bandsListType = new TypeToken<ArrayList<Band>>(){}.getType();

        public BandsJsonRequest(Activity activity) {
            super(activity);
        }

        @Override
        public void doStuff() {
            try {
                bandsJsonArrray = responseData.getJSONArray("bands");
                bandsList = gson.fromJson(String.valueOf(bandsJsonArrray), bandsListType);
                if (bandsList.size() > 0) {
                    page = new Page(
                            currentPage + 1,
                            ITEMS_PER_PAGE,
                            responseData.getInt("currentPageItemsCount"),
                            responseData.getInt("pagesCount") + offlinePage.getPages(),
                            responseData.getInt("totalItemsCount") + offlinePage.getItemsTotal(),
                            bandsList
                    );
                    add(page);
                }
                SearchFieldProgress.stop();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void performSearch(String s) {
        offlinePage = bandsWrapperOffline.performNonModifyingSearch(s);
        handle(offlinePage);
        SearchFieldProgress.start();
        if (offlinePage.getItemsOnCurrentPage() < bandsWrapperOffline.ITEMS_PER_PAGE) {
            bandsJsonRequest.fetch(QUERY_URL + s);
        } else {
            SearchFieldProgress.stop();
        }
    }

    @Override
    public void loadNextContent() {
        connectionTester.getConnectionMethod();
        if (connectionTester.isConnectionChanged()) {
            if (connectionTester.isConnectionAvailable()) {
                doLoad();
            } else {
                // toast ze pripojenie je fuc, eventualne refresh a dat do offline rezimu
            }
        } else {
            // ak program bezi tu, musi byt online
            doLoad();
        }
    }

    private void doLoad() {
        offlinePage = bandsWrapperOffline.performNonModifyingSearch(searchString, nextPageToLoad);
        if (offlinePage.getItemsOnCurrentPage() == bandsWrapperOffline.ITEMS_PER_PAGE) {
            handle(offlinePage);
        } else {
            if (offlinePage.getItemsOnCurrentPage() > 0) {
                handle(offlinePage);
            }
            query = QUERY_URL + searchString + "&p=" + (nextPageToLoad - offlinePage.getPages());
            bandsJsonRequest.fetch(query);
        }
    }

    @Override
    public int setDataSourceType() {
        return DATA_SOURCE_INTERNET;
    }

}
