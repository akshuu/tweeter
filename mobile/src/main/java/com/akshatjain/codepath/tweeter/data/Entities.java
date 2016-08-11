package com.akshatjain.codepath.tweeter.data;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by akshatjain on 8/6/16.
 */
@Parcel
public class Entities {

    @SerializedName("urls")
    public List<Url> urls;

    @SerializedName("media")
    public List<Media> medias;

    @SerializedName("user_mentions")
    public List<UserMentions> userMentions;

    public Entities() {
    }

    public Entities(List<Media> medias) {
        this.medias = medias;
    }

    public Entities(List<Url> urls, List<Media> medias) {
        this.urls = urls;
        this.medias = medias;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public List<Media> getMedias() {
        return medias;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Entities{");
        if(medias != null)
        for(Media media: medias)
            sb.append(media.toString());
        sb.append('}');
        if(urls != null)
            for(Url url : urls)
                sb.append(url.toString());
        sb.append('}');
        return sb.toString();
    }
}
