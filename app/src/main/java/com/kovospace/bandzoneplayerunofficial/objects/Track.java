package com.kovospace.bandzoneplayerunofficial.objects;

import com.kovospace.bandzoneplayerunofficial.helpers.Misc;
import com.kovospace.bandzoneplayerunofficial.helpers.Mp3Duration;
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
    private String slugRef;
    private String trackFullLocalPath;
    private boolean trackAvailableOffline;
    // sent by the API (v2), null while the backend has not read it yet
    private Long durationMs;
    // read out of the downloaded file, computed at most once per track
    private Long localDurationMs;

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

    public String getTrackFullLocalPath() {
        return trackFullLocalPath;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public void setTrackFullLocalPath(String trackFullLocalPath) {
        this.trackFullLocalPath = trackFullLocalPath;
    }

    public void setTrackFullLocalPath(Mp3File mp3File) {
        this.trackFullLocalPath = mp3File.getWorkingDirectoryPath() + "/" + slugRef + "/" + getFileName();
    }

    // has to stay the single source of the on-disk name - TracksAdapter hands
    // the very same string to the downloader, otherwise the saved copy would
    // never be found again and the track would look undownloaded forever
    public String getFileName() {
        return Misc.sanitizeFileName(title) + ".mp3";
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

    public String getSlugRef() {
        return slugRef;
    }

    public void setSlugRef(String slugRef) {
        this.slugRef = slugRef;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    /** Best duration already at hand, without touching the disk. Null means nothing known yet. */
    public Long getKnownDurationMs() {
        return localDurationMs != null ? localDurationMs : durationMs;
    }

    /**
     * Reads the exact duration out of the downloaded file. Does real I/O - call it off the main
     * thread. Result is cached, so repeat calls are free.
     */
    public Long resolveLocalDurationMs() {
        if (localDurationMs == null && trackAvailableOffline) {
            localDurationMs = Mp3Duration.read(getTrackFullLocalPath());
        }
        return localDurationMs;
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
