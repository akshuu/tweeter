package com.akshatjain.codepath.tweeter.data;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by akshatjain on 8/6/16.
 */
@Parcel
public class Media {
    @SerializedName("id")
    public long id;

    @SerializedName("media_url_https")
    public String mediaUrl;

    @SerializedName("url")
    public String tweetURL;

    public Media() {
    }

    public Media(long id, String mediaUrl, String tweetURL) {
        this.id = id;
        this.mediaUrl = mediaUrl;
        this.tweetURL = tweetURL;
    }

    public long getId() {
        return id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getTweetURL() {
        return tweetURL;
    }

    @Override
    public String toString() {
        return "Media{" +
                "mediaUrl='" + mediaUrl + '\'' +
                '}';
    }
}
