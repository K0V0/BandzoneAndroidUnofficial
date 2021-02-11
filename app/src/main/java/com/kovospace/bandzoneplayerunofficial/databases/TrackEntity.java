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

    @ColumnInfo(name = "trackTitle")
    private String title;

    @ColumnInfo(name = "bandSlug")
    private String bandSlug;

    public TrackEntity() {}

    public TrackEntity(Track track) {
        this.title = track.getTitle();
        this.bandSlug = track.getBandSlug();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBandSlug() {
        return bandSlug;
    }

    public void setBandSlug(String bandSlug) {
        this.bandSlug = bandSlug;
    }

    @Override
    public String toString() {
        return "id=" + id + " title=" + title + " bandSlug=" + bandSlug;
    }
}
