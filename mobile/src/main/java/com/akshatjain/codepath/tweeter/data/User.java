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

    @SerializedName("description")
    String description;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", likes=" + likes +
                ", description='" + description + '\'' +
                '}';
    }
}
