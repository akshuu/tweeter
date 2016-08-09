package com.akshatjain.codepath.tweeter.data;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by akshatjain on 8/4/16.
 */
@Parcel
public class User {

    @SerializedName("id")
    public long id;

    @SerializedName("name")
    public String name;

    @SerializedName("favourites_count")
    public int likes;

    @SerializedName("screen_name")
    public String screenName;

    @SerializedName("description")
    public String description;

    @SerializedName("profile_image_url_https")
    public String profileImageUrl;

    public User() {
    }

    public User(long id, String name, int likes, String screenName, String description, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.likes = likes;
        this.screenName = screenName;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
    }


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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
//                ", likes=" + likes +
//                ", description='" + description + '\'' +
                ", screen name='" + screenName +'\'' +
                '}';
    }
}
