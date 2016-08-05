package com.akshatjain.codepath.tweeter.retrofit;

import com.akshatjain.codepath.tweeter.data.Tweet;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by akshatjain on 8/3/16.
 */
public interface TweetsService {

    @GET("1.1/statuses/home_timeline.json")
    public Call<Tweet> getHomeTweets();
}
