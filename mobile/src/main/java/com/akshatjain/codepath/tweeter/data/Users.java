package com.akshatjain.codepath.tweeter.data;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by akshatjain on 8/13/16.
 */
@Parcel
public class Users {

    @SerializedName("users")
    public List<User> users;


    public Users() {
    }
}
