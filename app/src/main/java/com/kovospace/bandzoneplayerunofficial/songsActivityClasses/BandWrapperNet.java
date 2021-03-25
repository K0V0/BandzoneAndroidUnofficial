package com.kovospace.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.helpers.JsonRequest;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class BandWrapperNet extends BandWrapper {
    private final String QUERY_URL = "http://172.104.155.216:4000/bandzone/band?q=";
    private BandJsonRequest bandJsonRequest;

    public BandWrapperNet(Activity activity, Context context, String extra) {
        super(activity, context, extra);
        bandJsonRequest = new BandJsonRequest(this.activity, QUERY_URL + this.extra);
        bandJsonRequest.fetch();
    }

    @Override
    public int setDataSourceType() {
        return DATA_SOURCE_INTERNET;
    }

    public class BandJsonRequest extends JsonRequest {
        private Gson gson = new Gson();
        private JSONArray tracksJsonArrray = new JSONArray();
        private Type tracksListType = new TypeToken<ArrayList<Track>>(){}.getType();

        public BandJsonRequest(Activity activity, String extra) {
            super(activity, extra);
        }

        @Override
        public void doStuff() {
            try {
                band = new Band(
                        responseData.getString("title"),
                        responseData.getString("city"),
                        responseData.getString("imageUrl"),
                        responseData.getString("href"),
                        extra,
                        responseData.getString("genre"),
                        responseData.getString("platform")
                );
                tracksJsonArrray = responseData.getJSONArray("tracks");
                tracks = gson.fromJson(String.valueOf(tracksJsonArrray), tracksListType);
                triggerShow();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
