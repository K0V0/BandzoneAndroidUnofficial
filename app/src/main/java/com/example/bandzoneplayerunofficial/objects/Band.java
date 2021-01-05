package com.example.bandzoneplayerunofficial.objects;

public class Band {
    private String title;
    private String city;
    private String image_url;
    private String href;
    private String slug;
    private String genre;

    public Band(String title, String city, String image_url, String href, String slug, String genre, boolean dummy) {
        this.title = title;
        this.city = city;
        this.image_url = image_url;
        this.href = href;
        this.slug = slug;
        this.genre = genre;
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
}
