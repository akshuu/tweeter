package com.akshatjain.codepath.tweeter.data;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by akshatjain on 8/9/16.
 */
@Parcel
public class UserMentions {

    @SerializedName("screen_name")
    public String screenName;

    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public long id;


    public UserMentions() {
    }

    public UserMentions(String screenName, String name, long id) {
        this.screenName = screenName;
        this.name = name;
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
