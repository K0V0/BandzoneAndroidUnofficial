package com.kovospace.bandzoneplayerunofficial.objects;

import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.songsActivityClasses.ImageFile;

import java.io.File;

public class Band implements BandProfileItem {
    private String title;
    private String city;
    private String image_url;
    private String href;
    private String slug;
    private String genre;
    private String imageFullLocalPath;
    private boolean imageAvailableOffline;
    //private String recent_album;
    //private Pattern pattern;
    //private Matcher matcher;

    public Band(String title, String city, String image_url, String href, String slug, String genre) {
        this.title = title;
        this.city = city;
        this.image_url = image_url;
        this.href = href;
        this.slug = slug;
        this.genre = genre;
        //this.pattern = Pattern.compile("\\/[A-Za-z0-9_\\-\\.]+$", Pattern.CASE_INSENSITIVE);
        //this.matcher = this.pattern.matcher(this.href);
        //this.recent_album = "";
        //afterInit();
    }

    //private void afterInit() {
        //if (this.slug == null) {
            /*if (matcher.find()) {
                System.out.println(matcher);
            }*/
        //}
       // System.out.println(this.slug);
    //}

    public Band() {
        new Band(null, null, null, null, null, null);
    }

    public String getTitle() {
        return title;
    }

    /*@Override
    public String getAlbum() {
        return recent_album;
    }*/

    public String getCity() {
        return city;
    }

    public String getGenre() {
        return genre;
    }

    public String getSlug() {
        return slug;
    }

    public String getHref() {
        return href;
    }

    public String getImage_url() {
        return image_url;
    }

    @Override
    public String toString() {
        return "Band [name=" + title + "]";
    }

    @Override
    public boolean contains(BandProfileItem o) {
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
            return getImage_url();
        }
    }

    @Override
    public boolean hasOfflineCopy() {
        File file = new File(getImageFullLocalPath());
        imageAvailableOffline = file.exists() && !file.isDirectory();
        return imageAvailableOffline;
    }
}
