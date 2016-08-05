package com.akshatjain.codepath.tweeter.data;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by akshatjain on 8/4/16.
 */
public class Tweet {

    @SerializedName("profile_image_url_https")
    String profileImage;

    @SerializedName("id")
    long id;

    @SerializedName("screen_name")
    String handle;

    @SerializedName("created_at")
    String created_at;

    @SerializedName("text")
    String text;        // actual tweet

    @SerializedName("retweet_count")
    int retweet_count;

    @SerializedName("favorited")
    boolean isFavorite;

    @SerializedName("user")
    User userDetails;

    @SerializedName("favorite_count")
    private int favoriteCount;

    @Override
    public String toString() {
        return "Tweet{" +
                "profileImage='" + profileImage + '\'' +
                ", id=" + id +
                ", handle='" + handle + '\'' +
                ", created_at='" + created_at + '\'' +
                ", text='" + text + '\'' +
                ", retweet_count=" + retweet_count +
                ", isFavorite=" + isFavorite +
                ", userDetails=" + userDetails.toString() +
                '}';
    }



}
