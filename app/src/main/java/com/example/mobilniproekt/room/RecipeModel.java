package com.example.mobilniproekt.room;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public class RecipeModel {

    @PrimaryKey
    @NonNull
    private int id;

    private String Title;
    private String ImageLink;
    private String Publisher;
    private float SocialRank;
    private String f2f_link;
    private String Publisher_Url;
    private String RecipeId;
    private String SourceURL;

    public RecipeModel(int id, String title, String imageLink, String publisher, float socialRank, String f2f_link, String publisher_Url, String recipeId, String sourceURL) {
        this.id = id;
        Title = title;
        ImageLink = imageLink;
        Publisher = publisher;
        SocialRank = socialRank;
        this.f2f_link = f2f_link;
        Publisher_Url = publisher_Url;
        RecipeId = recipeId;
        SourceURL = sourceURL;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public String getPublisher() {
        return Publisher;
    }

    public float getSocialRank() {
        return SocialRank;
    }

    public String getF2f_link() {
        return f2f_link;
    }

    public String getPublisher_Url() {
        return Publisher_Url;
    }

    public String getRecipeId() {
        return RecipeId;
    }

    public String getSourceURL() {
        return SourceURL;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public void setSocialRank(float socialRank) {
        SocialRank = socialRank;
    }

    public void setF2f_link(String f2f_link) {
        this.f2f_link = f2f_link;
    }

    public void setPublisher_Url(String publisher_Url) {
        Publisher_Url = publisher_Url;
    }

    public void setRecipeId(String recipeId) {
        RecipeId = recipeId;
    }

    public void setSourceURL(String sourceURL) {
        SourceURL = sourceURL;
    }

}
