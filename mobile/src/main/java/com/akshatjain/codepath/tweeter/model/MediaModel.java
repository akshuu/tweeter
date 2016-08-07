package com.akshatjain.codepath.tweeter.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by akshatjain on 8/6/16.
 */
@Table(name = "media")
public class MediaModel extends Model {
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;

    @Column(name = "media_url")
    public String mediaURL;

    public MediaModel() {
    }

    public MediaModel(long remoteId, String mediaURL) {
        this.remoteId = remoteId;
        this.mediaURL = mediaURL;
    }

    public List<TweetModel> medias(){
        {
            return getMany(TweetModel.class,"media");
        }
    }
}
