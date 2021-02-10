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

    // mozem dat konstruktor

    public BandEntity() {}

    public BandEntity(Band band) {
        this.title = band.getTitle();
        this.slug = band.getSlug();
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return "id=" + id + " slug=" + slug;
    }
}
