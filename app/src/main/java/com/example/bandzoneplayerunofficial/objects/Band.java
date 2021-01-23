package com.example.bandzoneplayerunofficial.objects;

import com.example.bandzoneplayerunofficial.interfaces.BandProfileItem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Band implements BandProfileItem {
    private String title;
    private String city;
    private String image_url;
    private String href;
    private String slug;
    private String genre;
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
}
