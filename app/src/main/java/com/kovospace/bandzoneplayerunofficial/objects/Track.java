package com.kovospace.bandzoneplayerunofficial.objects;

import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.Mp3File;

import java.io.File;

public class Track implements BandProfileItem {
    private String albumTitle;
    private String albumLabel;
    private String albumReleaseYear;
    private String title;
    private String href;
    private String hrefHash;
    private int order;
    private boolean playing;
    private boolean paused;
    private String bandName;
    private String bandSlug;
    private String trackFullLocalPath;
    private boolean trackAvailableOffline;

    public Track() {}

    public Track(String title, String href, String hrefHash, String albumLabel, String albumTitle, String albumReleaseYear) {
        this.title = title;
        this.href = href;
        this.hrefHash = hrefHash;
        this.albumLabel = albumLabel;
        this.albumTitle = albumTitle;
        this.albumReleaseYear = albumReleaseYear;
    }

    public String getAlbumTitle() { return albumTitle; }

    public void setAlbumTitle(String albumTitle) { this.albumTitle = albumTitle; }

    public String getAlbumLabel() { return albumLabel; }

    public void setAlbumLabel(String albumLabel) { this.albumLabel = albumLabel; }

    public String getAlbumReleaseYear() { return albumReleaseYear; }

    public void setAlbumReleaseYear(String albumReleaseYear) { this.albumReleaseYear = albumReleaseYear; }

    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

    public String getHrefHash() {
        return hrefHash;
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
