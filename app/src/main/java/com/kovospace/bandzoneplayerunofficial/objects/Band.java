package com.kovospace.bandzoneplayerunofficial.objects;

import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.ImageFile;

import java.io.File;

public class Band implements BandProfileItem {
    private String title;
    private String city;
    private String imageUrl;
    private String slug;
    private String genre;
    private String platform;
    private String imageFullLocalPath;
    private boolean imageAvailableOffline;
    private boolean fromDb;

    public Band(String title, String city, String imageUrl, String slug, String genre, String platform) {
        this.title = title;
        this.city = city;
        this.imageUrl = imageUrl;
        this.slug = slug;
        this.genre = genre;
        this.platform = platform;
    }

    public Band() {
        new Band(null, null, null, null, null, null);
    }

    public String getTitle() {
        return title;
    }

    public String getCity() {
        return city;
    }

    public String getGenre() {
        return genre;
    }

    public String getSlug() {
        return slug;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPlatform() { return platform; }

    @Override
    public String toString() {
        return "Band [name=" + title + "]";
    }

    @Override
    public boolean contains(BandProfileItem o) {
        return this.equals(o);
    }

    @Override
    public boolean equals(Object o) {
        return this.slug.equals(((Band) o).getSlug());
    }

    public String getImageFullLocalPath() {
        return imageFullLocalPath;
    }

    public void setImageFullLocalPath(String path) {
        this.imageFullLocalPath = path;
    }

    public void setImageFullLocalPath(ImageFile imageFile) {
        setImageFullLocalPath(imageFile.getWorkingDirectoryPath() + "/" + slug + "/" + title + ".jpg");
    }

    public boolean isImageAvailableOffline() {
        return imageAvailableOffline;
    }

    @Override
    public String getLocalOrHref() {
        if (imageAvailableOffline) {
            return getImageFullLocalPath();
        } else {
            return getImageUrl();
        }
    }

    @Override
    public boolean hasOfflineCopy() {
        File file = new File(getImageFullLocalPath());
        imageAvailableOffline = file.exists() && !file.isDirectory();
        return imageAvailableOffline;
    }

    @Override
    public boolean isAvailableOffline() {
        return imageAvailableOffline;
    }

    public boolean isFromDb() {
        return fromDb;
    }

    public void setFromDb(boolean fromDb) {
        this.fromDb = fromDb;
    }
}
