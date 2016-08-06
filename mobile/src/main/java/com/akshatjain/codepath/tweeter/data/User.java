package com.akshatjain.codepath.tweeter.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akshatjain on 8/4/16.
 */
public class User {

    @SerializedName("name")
    String name;

    @SerializedName("favourites_count")
    int likes;

    @SerializedName("screen_name")
    String screenName;

    @SerializedName("description")
    String description;

    @SerializedName("profile_image_url_https")
    String profileImageUrl;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", likes=" + likes +
                ", description='" + description + '\'' +
                ", screen name='" + screenName +'\'' +
                '}';
    }
}
