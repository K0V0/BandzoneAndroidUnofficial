package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.example.bandzoneplayerunofficial.helpers.JsonRequest;
import com.example.bandzoneplayerunofficial.objects.Band;
import com.example.bandzoneplayerunofficial.objects.Track;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BandWrapperNet extends BandWrapper {
    private final String QUERY_URL = "http://172.104.155.216:3030/band/";
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
                        responseData.getString("image_url"),
                        responseData.getString("href"),
                        extra,
                        responseData.getString("genre")
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
