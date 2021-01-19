package com.example.bandzoneplayerunofficial.mainActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.example.bandzoneplayerunofficial.MainActivity;
import com.example.bandzoneplayerunofficial.helpers.*;

public class BandsSearch extends OnFinishTypingHelper {

    private TestConnection testConnection;
    private BandsWrapper bandsWrapper;
    //private BandsWrapperNet bandsWrapper;
    private String searchText;
    private Context context;
    private Activity mainActivity;

    //private int itemsOnViewport;
    //private int itemsOnActualPage;
    //private boolean stopMultipleLoads = false;

    public BandsSearch(MainActivity mainActivity, Context context) {
        super();
        this.mainActivity = mainActivity;
        this.context = context;
        this.testConnection = new TestConnection(this.context);

        if (testConnection.isActive()) {
            this.bandsWrapper = new BandsWrapperNet(mainActivity, context);
        } else {
            this.bandsWrapper = new BandsWrapperOffline();
        }
        //afterConstruct();
        //doJsonRequest(); // let bandzone to load random bands on first run
    }

    @Override
    public void doStuffNotOften() {

        searchText = getText();
        if (searchText.length() == 0) {
            bandsWrapper.clear();
        } else {
            bandsWrapper.search(searchText);
        }
        // only runs on write, no need to page load limit, always 1
        //this.nextPageToLoad = 1;
        //doJsonRequest();

        // let bandzone choose some bands when searchfield deleted
        /*if ((this.getText().length() == 0)&&(this.bandsList.size() > 0)) {
            this.bandsList.clear();
            this.mAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void doStuffOnZero() {
        bandsWrapper.clear();
    }




   /* private void jsonParseBands(JSONObject json) {
        if (itemsTotal == 0) {
            // žiadne kapely
            toastMessage.send(R.string.noBands);
        } else {
            // some bands found
            JSONArray arr = new JSONArray();
            try {
                arr = json.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if ((this.bandsList == null) || (this.currentPage == 1)) {
                // first load
                this.bandsList.clear();
                this.bandsList.addAll(gson.fromJson(String.valueOf(arr), this.bandListType));
                this.mAdapter.notifyDataSetChanged();
                layoutManager.smoothScrollToPosition(mRecyclerView, new RecyclerView.State(), 0);
            } else {
                // next pages
                //removeLoadingDialog();
                bandsList.addAll(gson.fromJson(String.valueOf(arr), bandListType));
                mAdapter.notifyItemInserted(bandsList.size() - 1);
                layoutManager.smoothScrollToPosition(
                        mRecyclerView,
                        new RecyclerView.State(),
                        //calculateScroll(mRecyclerView)
                );
            }
        }
        stopMultipleLoads = false;
    }*/


    /*
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
        this.itemsOnViewport = Misc.getVisibleItemCount(mRecyclerView);
        return bandsList.size() - (this.itemsOnActualPage - this.itemsOnViewport+2);
    }*/

}
