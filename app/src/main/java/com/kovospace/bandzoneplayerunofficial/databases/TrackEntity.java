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

    @ColumnInfo(name = "trackHref")
    private String href;

    @ColumnInfo(name = "trackHrefHash")
    private String hrefHash;

    @ColumnInfo(name = "bandSlug")
    private String bandSlug;

    @ColumnInfo(name = "albumTitle")
    private String albumTitle;

    @ColumnInfo(name = "albumLabel")
    private String albumLabel;

    @ColumnInfo(name = "albumReleaseYear")
    private String albumReleaseYear;

    public TrackEntity() {}

    public TrackEntity(Track track) {
        this.title = track.getTitle();
        this.href = track.getHref();
        this.hrefHash = track.getHrefHash();
        this.bandSlug = track.getSlugRef();
        this.albumLabel = track.getAlbumLabel();
        this.albumTitle = track.getAlbumTitle();
        this.albumReleaseYear = track.getAlbumReleaseYear();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

    public String getHrefHash() {
        return hrefHash;
    }

    public String getBandSlug() {
        return bandSlug;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setHrefHash(String hrefHash) {
        this.hrefHash = hrefHash;
    }

    public void setBandSlug(String bandSlug) {
        this.bandSlug = bandSlug;
    }

    public String getAlbumTitle() { return albumTitle; }

    public void setAlbumTitle(String albumTitle) { this.albumTitle = albumTitle; }

    public String getAlbumLabel() { return albumLabel; }

    public void setAlbumLabel(String albumLabel) { this.albumLabel = albumLabel; }

    public String getAlbumReleaseYear() { return albumReleaseYear; }

    public void setAlbumReleaseYear(String albumReleaseYear) { this.albumReleaseYear = albumReleaseYear; }

    @Override
    public String toString() {
        return "id=" + id + " title=" + title + " bandSlug=" + bandSlug;
    }
}
