package com.finki.mobilniproekt.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class RecipeModel {

    @NonNull
    @PrimaryKey
    private String recipe_id;

    private String publisher;

    private String f2f_url;

    private String title;

    private String source_url;

    private String image_url;

    private Float social_rank;

    private String publisher_url;

    public RecipeModel(){

    }

    public RecipeModel(@NonNull String recipe_id, String publisher, String f2f_url, String title, String source_url, String image_url, Float social_rank, String publisher_url) {
        this.recipe_id = recipe_id;
        this.publisher = publisher;
        this.f2f_url = f2f_url;
        this.title = title;
        this.source_url = source_url;
        this.image_url = image_url;
        this.social_rank = social_rank;
        this.publisher_url = publisher_url;
    }

    @NonNull
    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(@NonNull String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getF2f_url() {
        return f2f_url;
    }

    public void setF2f_url(String f2f_url) {
        this.f2f_url = f2f_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Float getSocial_rank() {
        return social_rank;
    }

    public void setSocial_rank(Float social_rank) {
        this.social_rank = social_rank;
    }

    public String getPublisher_url() {
        return publisher_url;
    }

    public void setPublisher_url(String publisher_url) {
        this.publisher_url = publisher_url;
    }
}
