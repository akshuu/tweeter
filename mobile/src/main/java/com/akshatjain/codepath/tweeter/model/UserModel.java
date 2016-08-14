package com.akshatjain.codepath.tweeter.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by akshatjain on 8/4/16.
 */
@Table(name = "UserModel")
public class UserModel extends Model {


    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;

    @Column(name = "Name")
    public String name;

    @Column(name = "favourites_count")
    public int likes;

    @Column(name = "screen_name")
    public String screenName;

    @Column(name = "description")
    public String description;

    @Column(name = "image_url")
    public String profileImageUrl;

    @Column(name = "followers")
    public long followers;

    @Column(name = "friends")
    public long friends;

    public UserModel() {
        super();
    }


    public UserModel(long remoteId, String name, int likes, String screenName, String description, String profileImageUrl, long followers, long friends) {
        this.remoteId = remoteId;
        this.name = name;
        this.likes = likes;
        this.screenName = screenName;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
        this.followers = followers;
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                " id = " + remoteId +
                "name='" + name + '\'' +
                ", likes=" + likes +
                ", description='" + description + '\'' +
                ", screen name='" + screenName +'\'' +
                '}';
    }

    public static List<UserModel> getUserByScreenName(String screenName){
        return new Select()
                .from(UserModel.class)
                .where("screen_name = ?",screenName)
                .execute();
    }
    public List<TweetModel> tweets(){
        return getMany(TweetModel.class,"user");
    }
}
