package com.akshatjain.codepath.tweeter.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akshatjain on 8/6/16.
 */
public class Url {
    @SerializedName("url")
    public String url;

    public Url(String url) {
        this.url = url;
    }
}
