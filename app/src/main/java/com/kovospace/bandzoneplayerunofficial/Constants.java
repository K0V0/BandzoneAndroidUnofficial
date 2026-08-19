package com.kovospace.bandzoneplayerunofficial;

public final class Constants {

    private Constants() {}

    private static final String API = "https://music-pages-scraper.matejkovac.sk";

    // Endpoints are versioned one by one on the backend. Only the band profile moved to v2, where
    // every track carries durationMs; the bands listing is unchanged and stays on v1.
    public static final String BANDS_LIST_QUERY = API + "/v1/bandzone/bands?q=";
    public static final String SONGS_LIST_QUERY = API + "/v2/bandzone/band?q=";
    // One track, read while the request waits - for the track being played when nothing else knows
    // how long it is. Takes the track's href, url-encoded.
    public static final String TRACK_DURATION_QUERY = API + "/v2/bandzone/track/duration?q=";
}
