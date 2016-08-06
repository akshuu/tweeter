package com.akshatjain.codepath.tweeter.data;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by akshatjain on 8/4/16.
 */
public class Tweet {

    @SerializedName("id")
    long id;

    @SerializedName("created_at")
    String created_at;

    @SerializedName("text")
    String text;        // actual tweet

    @SerializedName("retweet_count")
    int retweet_count;

    @SerializedName("favorited")
    boolean isFavorite;

    @Override
    public String toString() {
        return "Tweet{" +
                ", id=" + id +
                ", created_at='" + created_at + '\'' +
                ", text='" + text + '\'' +
                ", retweet_count=" + retweet_count +
                ", isFavorite=" + isFavorite +
                ", isRetweeted=" + isRetweeted +
                ", userDetails=" + userDetails.toString() +
                ", favoriteCount=" + favoriteCount +
                '}';
    }

    @SerializedName("retweeted")
    boolean isRetweeted;

    @SerializedName("user")
    User userDetails;

    @SerializedName("favorite_count")
    private int favoriteCount;


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

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public void setRetweeted(boolean retweeted) {
        isRetweeted = retweeted;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public User getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(User userDetails) {
        this.userDetails = userDetails;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
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

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
