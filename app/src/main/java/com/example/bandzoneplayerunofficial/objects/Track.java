package com.example.bandzoneplayerunofficial.objects;

import com.example.bandzoneplayerunofficial.interfaces.BandProfileItem;

public class Track implements BandProfileItem {
    private String full_title;
    private String title;
    private String album;
    private int plays_count;
    private String href;
    private String href_hash;
    private String duration;
    private int order;
    private boolean playing;

    public Track(String full_title, String title, String album, int plays_count, String href, String href_hash, String duration) {
        this.full_title = full_title;
        this.title = title;
        this.album = album;
        this.plays_count = plays_count;
        this.href = href;
        this.href_hash = href_hash;
        this.duration = duration;
    }

    public String getFull_title() {
        return full_title;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public int getPlays_count() {
        return plays_count;
    }

    public String getHref() {
        return href;
    }

    public String getHref_hash() {
        return href_hash;
    }

    public String getDuration() {
        return duration;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    @Override
    public String toString() {
        return "Track [name=" + title + "]";
    }
}
