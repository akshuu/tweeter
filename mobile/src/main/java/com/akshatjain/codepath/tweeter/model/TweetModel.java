package com.akshatjain.codepath.tweeter.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by akshatjain on 8/4/16.
 */
@Table(name="TweetModel")
public class TweetModel extends Model {

    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;

    @Column(name = "created_at")
    public String created_at;

    @Column(name = "text")
    public String text;        // actual tweet

    @Column(name = "retweet_count")
    public int retweet_count;

    @Column(name = "favorited")
    public boolean isFavorite;

    @Column(name = "retweeted")
    public boolean isRetweeted;

    @Column(name = "favorite_count")
    public  int favoriteCount;

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public UserModel userModel;

    @Column(name = "entities", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public EntitiesModel entitiesModel;

    public TweetModel() {
    }

    public TweetModel(long remoteId, String created_at, String text, int retweet_count, boolean isFavorite, boolean isRetweeted, int favoriteCount, UserModel userModel, EntitiesModel entitiesModel) {
        this.remoteId = remoteId;
        this.created_at = created_at;
        this.text = text;
        this.retweet_count = retweet_count;
        this.isFavorite = isFavorite;
        this.isRetweeted = isRetweeted;
        this.favoriteCount = favoriteCount;
        this.userModel = userModel;
        this.entitiesModel = entitiesModel;
    }

    public static List<TweetModel> getAllTweets() {
        // This is how you execute a query
        return new Select()
                .from(TweetModel.class)
                .execute();
    }

}
