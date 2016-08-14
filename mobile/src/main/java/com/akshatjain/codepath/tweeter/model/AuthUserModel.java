package com.akshatjain.codepath.tweeter.model;

import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by akshatjain on 8/13/16.
 */
@Table( name = "authuser")
public class AuthUserModel extends UserModel{

    public AuthUserModel() {
    }

    public AuthUserModel(long remoteId, String name, int likes, String screenName, String description, String profileImageUrl, long followers, long friends) {
        super(remoteId, name, likes, screenName, description, profileImageUrl, followers, friends);
    }

    public static AuthUserModel getLoggedInUser(){
        return new Select()
                .from(AuthUserModel.class)
                .executeSingle();
    }
}
