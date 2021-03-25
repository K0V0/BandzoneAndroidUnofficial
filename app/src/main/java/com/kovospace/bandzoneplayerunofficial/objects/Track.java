package com.kovospace.bandzoneplayerunofficial.objects;

import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.Mp3File;

import java.io.File;

public class Track implements BandProfileItem {
    private String fullTitle;
    private String title;
    private String album;
    private int playsCount;
    private String href;
    private String hrefHash;
    private String duration;
    private long durationMilisecs;
    private int order;
    private boolean playing;
    private boolean paused;
    private String bandName;
    private String bandSlug;
    private String trackFullLocalPath;
    private boolean trackAvailableOffline;

    public Track(String fullTitle, String title, String album, int playsCount, String href, String hrefHash, String duration) {
        this.fullTitle = fullTitle;
        this.title = title;
        this.album = album;
        this.playsCount = playsCount;
        this.href = href;
        this.hrefHash = hrefHash;
        this.duration = duration;
        this.durationMilisecs = durationToMilisec();
    }

    private long durationToMilisec() {
        return durationToMilisec(this.duration);
    }

    private long durationToMilisec(String duration) {
        long result = 0;
        String[] parts = duration.split("\\:",0);
        for (int i = 0; i < parts.length - 1; i++) {
            result += (long) Integer.parseInt(parts[i]) * Math.pow(60, parts.length-i-1) * 1000;
        }
        result += (long) Integer.parseInt(parts[parts.length-1]) * 1000;
        return result;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public int getPlaysCount() {
        return playsCount;
    }

    public String getHref() {
        return href;
    }

    public String getHrefHash() {
        return hrefHash;
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

    public void convertDuration() {
        this.durationMilisecs = durationToMilisec();
    }

    public boolean hasOfflineCopy() {
        File file = new File(getTrackFullLocalPath());
        trackAvailableOffline = file.exists() && !file.isDirectory();
        return trackAvailableOffline;
    }

    public String getLocalOrHref() {
        if (trackAvailableOffline) {
            return getTrackFullLocalPath();
        } else {
            return getHref();
        }
    }

    public boolean isAvailableOffline() {
        return trackAvailableOffline;
    }

    @Override
    public String toString() {
        return "Track [name=" + title + "]";
    }

    @Override
    public boolean contains(BandProfileItem o) {
        return this.hrefHash.equals(((Track) o).getHrefHash());
    }
}
