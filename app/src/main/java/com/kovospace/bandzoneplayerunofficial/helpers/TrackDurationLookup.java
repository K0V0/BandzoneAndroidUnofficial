package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kovospace.bandzoneplayerunofficial.Constants;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Asks the backend for the playing time of a track nothing else could measure.
 *
 * <p>Last resort, in this order:
 *
 * <ol>
 *   <li><b>The band profile again.</b> The backend reads the durations of a band's tracks in the
 *       background after the first request for it, so a profile opened a minute ago usually has
 *       them by now. One cheap call covers the whole band.
 *   <li><b>The single-track endpoint.</b> It reads the audio while the request is open instead of
 *       queueing it, so it costs the backend real work - only for the track being played, and only
 *       when the repoll came back empty.
 * </ol>
 *
 * <p>Everything here is quiet: a duration that cannot be had is not worth a toast, the player just
 * keeps the time it already shows.
 */
public class TrackDurationLookup {

    public interface OnResolved {
        void onDuration(long durationMs);
    }

    // The single-track endpoint reads the audio while the request is open, so it answers in
    // seconds rather than milliseconds - Volley's 2.5 s default would give up on it.
    private static final int TIMEOUT_MS = 30000;

    private static final String TRACKS = "tracks";
    private static final String HREF_HASH = "hrefHash";
    private static final String DURATION_MS = "durationMs";

    private static RequestQueue queue;

    private final Context context;
    private final Band band;
    private final Track track;
    private final OnResolved onResolved;

    public TrackDurationLookup(Context context, Band band, Track track, OnResolved onResolved) {
        this.context = context;
        this.band = band;
        this.track = track;
        this.onResolved = onResolved;
    }

    public void resolve() {
        if (context == null || track == null || track.getHref() == null) {
            return;
        }
        if (band == null || band.getSlug() == null) {
            askForTrack();
            return;
        }
        repollBand();
    }

    private void repollBand() {
        get(Constants.SONGS_LIST_QUERY + band.getSlug(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Long durationMs = durationInBandProfile(response);
                if (durationMs != null) {
                    deliver(durationMs);
                } else {
                    askForTrack();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                askForTrack();
            }
        });
    }

    private void askForTrack() {
        String href = encode(track.getHref());
        if (href == null) {
            return;
        }
        get(Constants.TRACK_DURATION_QUERY + href, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if (!json.isNull(DURATION_MS)) {
                        deliver(json.getLong(DURATION_MS));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // nothing left to try - the player keeps whatever it is showing
            }
        });
    }

    // The track is matched by hrefHash rather than by title: an album can hold the same title twice.
    private Long durationInBandProfile(String response) {
        try {
            JSONArray tracks = new JSONObject(response).getJSONArray(TRACKS);
            for (int i = 0; i < tracks.length(); i++) {
                JSONObject json = tracks.getJSONObject(i);
                if (json.optString(HREF_HASH).equals(track.getHrefHash())
                        && !json.isNull(DURATION_MS)) {
                    return json.getLong(DURATION_MS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deliver(long durationMs) {
        if (durationMs <= 0) {
            return;
        }
        track.setDurationMs(durationMs);
        if (onResolved != null) {
            onResolved.onDuration(durationMs);
        }
    }

    private void get(String url, Response.Listener<String> onOk, Response.ErrorListener onError) {
        StringRequest request = new StringRequest(Request.Method.GET, url, onOk, onError);
        request.setRetryPolicy(
                new DefaultRetryPolicy(TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue(context).add(request);
    }

    private static synchronized RequestQueue queue(Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return queue;
    }

    private static String encode(String href) {
        try {
            return URLEncoder.encode(href, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
