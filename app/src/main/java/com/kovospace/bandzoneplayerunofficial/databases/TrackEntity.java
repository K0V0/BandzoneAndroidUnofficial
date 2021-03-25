package com.kovospace.bandzoneplayerunofficial.databases;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.kovospace.bandzoneplayerunofficial.objects.Track;

@Entity(tableName = "offlineTracks")
public class TrackEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "trackId")
    private int id;

    @ColumnInfo(name = "trackFullTitle")
    private String fullTitle;

    @ColumnInfo(name = "trackTitle")
    private String title;

    @ColumnInfo(name = "trackAlbum")
    private String album;

    @ColumnInfo(name = "trackPlays")
    private int plays_count;

    @ColumnInfo(name = "trackHref")
    private String href;

    @ColumnInfo(name = "trackHrefHash")
    private String hrefHash;

    @ColumnInfo(name = "trackDuration")
    private String duration;

    @ColumnInfo(name = "trackDurationMiliseconds")
    private long durationMilisecs;

    @ColumnInfo(name = "bandSlug")
    private String bandSlug;

    public TrackEntity() {}

    public TrackEntity(Track track) {
        this.fullTitle = track.getTitle();
        this.title = track.getTitle();
        this.album = track.getAlbum();
        this.plays_count = track.getPlaysCount();
        this.href = track.getHref();
        this.hrefHash = track.getHrefHash();
        this.duration = track.getDuration();
        this.bandSlug = track.getBandSlug();
    }

    public int getId() {
        return id;
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

    public int getPlays_count() {
        return plays_count;
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

    public String getBandSlug() {
        return bandSlug;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setPlays_count(int plays_count) {
        this.plays_count = plays_count;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setHrefHash(String hrefHash) {
        this.hrefHash = hrefHash;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDurationMilisecs(long durationMilisecs) {
        this.durationMilisecs = durationMilisecs;
    }

    public void setBandSlug(String bandSlug) {
        this.bandSlug = bandSlug;
    }

    @Override
    public String toString() {
        return "id=" + id + " title=" + title + " bandSlug=" + bandSlug;
    }
}
