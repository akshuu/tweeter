package com.akshatjain.codepath.tweeter.data;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by akshatjain on 8/4/16.
 */
@Parcel
public class Tweet{

    @SerializedName("id")
    public long id;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("text")
    public String text;        // actual tweet

    @SerializedName("retweet_count")
    public int retweet_count;

    @SerializedName("favorited")
    public boolean isFavorite;

    @SerializedName("retweeted")
    public boolean isRetweeted;

    @SerializedName("user")
    public User userDetails;

    @SerializedName("favorite_count")
    public  int favoriteCount;

    @SerializedName("entities")
    public Entities entities;

    public Tweet() {
    }

    public Tweet(long id, String created_at, String text, int retweet_count, boolean isFavorite, boolean isRetweeted, User userDetails, int favoriteCount) {
        this.id = id;
        this.created_at = created_at;
        this.text = text;
        this.retweet_count = retweet_count;
        this.isFavorite = isFavorite;
        this.isRetweeted = isRetweeted;
        this.userDetails = userDetails;
        this.favoriteCount = favoriteCount;
    }

    public Tweet(long id, String created_at, String text, int retweet_count, boolean isFavorite, boolean isRetweeted, User userDetails, int favoriteCount,Entities entities) {
        this.id = id;
        this.created_at = created_at;
        this.text = text;
        this.retweet_count = retweet_count;
        this.isFavorite = isFavorite;
        this.isRetweeted = isRetweeted;
        this.userDetails = userDetails;
        this.favoriteCount = favoriteCount;
        this.entities = entities;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public User getUserDetails() {
        return userDetails;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Entities getEntities() {
        return entities;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                ", id=" + id +
                ", created_at='" + created_at + '\'' +
                ", retweet_count=" + retweet_count +
                ", isFavorite=" + isFavorite +
                ", isRetweeted=" + isRetweeted +
                ", userDetails=" + userDetails.toString() +
                ", favoriteCount=" + favoriteCount +
                ", entities = " + ((entities != null ) ? entities.toString() : "") +
                ", text='" + text + '\'' +
                '}';
    }
}
