package com.kovospace.bandzoneplayerunofficial.databases;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.kovospace.bandzoneplayerunofficial.objects.Band;

@Entity(tableName = "offlineBands")
public class BandEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "bandId")
    private int id;

    @ColumnInfo(name = "bandTitle")
    private String title;

    @ColumnInfo(name = "bandSlug")
    private String slug;

    @ColumnInfo(name = "bandGenre")
    private String genre;

    @ColumnInfo(name = "bandCity")
    private String city;

    @ColumnInfo(name = "bandImageUrl")
    private String image_url;

    @ColumnInfo(name = "bandImageFullLocalPath")
    private String imageFullLocalPath;

    @ColumnInfo(name = "bandHref")
    private String href;

    public BandEntity() {}

    public BandEntity(Band band) {
        this.title = band.getTitle();
        this.city = band.getCity();
        this.image_url = band.getImageUrl();
        this.href = band.getHref();
        this.slug = band.getSlug();
        this.genre = band.getGenre();
        this.imageFullLocalPath = band.getImageFullLocalPath();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getGenre() {
        return genre;
    }

    public String getCity() {
        return city;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getImageFullLocalPath() {
        return imageFullLocalPath;
    }

    public String getHref() {
        return href;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setImageFullLocalPath(String imageFullLocalPath) {
        this.imageFullLocalPath = imageFullLocalPath;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "id=" + id + " slug=" + slug;
    }
}
