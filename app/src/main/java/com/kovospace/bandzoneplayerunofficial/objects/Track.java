package com.kovospace.bandzoneplayerunofficial.objects;

import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.Mp3File;

import java.io.File;

public class Track implements BandProfileItem {
    private String full_title;
    private String title;
    private String album;
    private int plays_count;
    private String href;
    private String href_hash;
    private String duration;
    private long durationMilisecs;
    private int order;
    private boolean playing;
    private String bandName;
    private String bandSlug;
    private String trackFullLocalPath;

    public Track(String full_title, String title, String album, int plays_count, String href, String href_hash, String duration) {
        this.full_title = full_title;
        this.title = title;
        this.album = album;
        this.plays_count = plays_count;
        this.href = href;
        this.href_hash = href_hash;
        this.duration = duration;
        this.durationMilisecs = durationToMilisec(duration);
    }

    private long durationToMilisec(String duration) {
        long result = 0;
        String[] parts = duration.split(":",0);
        for (int i = 0; i < parts.length - 1; i++) {
            result += (long) Integer.parseInt(parts[i]) * Math.pow(60, parts.length-i-1) * 1000;
        }
        result += (long) Integer.parseInt(parts[parts.length-1]) * 1000;
        return result;
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

    public long getDurationMilisecs() {
        return durationMilisecs;
    }

    public int getOrder() {
        return order;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public String getBandName() {
        return bandName;
    }

    public String getBandSlug() {
        return bandSlug;
    }

    public String getTrackFullLocalPath() {
        return trackFullLocalPath;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public void setBandSlug(String bandSlug) {
        this.bandSlug = bandSlug;
    }

    public void setTrackFullLocalPath(String trackFullLocalPath) {
        this.trackFullLocalPath = trackFullLocalPath;
    }

    public void setTrackFullLocalPath(Mp3File mp3File) {
        this.trackFullLocalPath = mp3File.getWorkingDirectoryPath() + "/" + bandSlug + "/" + title + ".mp3";
    }

    public boolean hasOfflineCopy() {
        File file = new File(getTrackFullLocalPath());
        return file.exists() && !file.isDirectory();
    }

    public String getLocalOrHref() {
        if (hasOfflineCopy()) {
            return getTrackFullLocalPath();
        } else {
            return getHref();
        }
    }

    @Override
    public String toString() {
        return "Track [name=" + title + "]";
    }

    @Override
    public boolean contains(BandProfileItem o) {
        return this.href_hash.equals(((Track) o).getHref_hash());
    }
}
