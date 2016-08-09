package com.akshatjain.codepath.tweeter.data;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by akshatjain on 8/6/16.
 */
@Parcel
public class Url {
    @SerializedName("url")
    public String url;

    public Url() {
    }

    public Url(String url) {
        this.url = url;
    }
}
