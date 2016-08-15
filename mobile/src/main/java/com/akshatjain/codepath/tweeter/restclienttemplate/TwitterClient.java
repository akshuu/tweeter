package com.akshatjain.codepath.tweeter.restclienttemplate;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.akshatjain.codepath.tweeter.utils.Constants;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;


/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "XlG91SI3K6uowY2O7kwGvjFvE";       // Change this
	public static final String REST_CONSUMER_SECRET = "6HpyPGNXM2xrOwEhLTNv7aOO3Yt5RmnxFjDBfuVgCaUpkT0Wsa"; // Change this
	public static final String REST_CALLBACK_URL = "x-oauthflow-twitter://github.com"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		Log.d(Constants.TAG,"URL == " + apiUrl);
		RequestParams params = new RequestParams();

		params.put("page", String.valueOf(page));
		client.get(apiUrl, params, handler);
	}

	public void postNewTweet(String tweet,long replyId, AsyncHttpResponseHandler handler){
		String url = getApiUrl("/statuses/update.json");
		RequestParams params = new RequestParams();

		params.put("status", tweet);
		if(replyId != -1){
			Log.d(Constants.TAG,"replying to tweet == " + replyId);

			params.put("in_reply_to_status_id", replyId);
		}
		Log.d(Constants.TAG,"Posting tweet to URL == " + url);

		client.post(url,params,handler);
	}

	public void getMentionTweets(String screenName, int page, long sinceId, long maxId, boolean isUserTweet, AsyncHttpResponseHandler handler) {
		if(isUserTweet){
			getUserTimeLine(screenName,handler);
			return;
		}
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		Log.d(Constants.TAG,"URL for mentioned tweet == " + apiUrl);
		RequestParams params = new RequestParams();
		if(sinceId != -1){
			params.put("since_id", String.valueOf(sinceId));
		}
		if(maxId != -1){
			params.put("max_id", String.valueOf(maxId));
		}
		params.put("page", String.valueOf(page));
		client.get(apiUrl, params, handler);
	}

	public void retweet(long retweetId, boolean bShouldRetweet, AsyncHttpResponseHandler handler) {
		String apiUrl;
		if(bShouldRetweet)
			apiUrl= getApiUrl("statuses/retweet/") + retweetId + ".json";
		else{
			apiUrl = getApiUrl("statuses/unretweet/") + retweetId + ".json";
		}
		Log.d(Constants.TAG,"URL == " + apiUrl);
		client.post(apiUrl, handler);

	}

	public void getUser(String screenName,AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		Log.d(Constants.TAG,"User details URL == " + apiUrl);
		client.get(apiUrl,params,handler);
	}

	public void getUserProfile(AsyncHttpResponseHandler handler) {
		String url = getApiUrl("account/verify_credentials.json");
		client.get(url,handler);
	}

	//Following List
	public void getFriendsList(String screenName,AsyncHttpResponseHandler handler) {
		String url = getApiUrl("friends/list.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		client.get(url,params,handler);
	}

	public void getFollowersList(String screenName,AsyncHttpResponseHandler handler) {
		String url = getApiUrl("followers/list.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		client.get(url,params,handler);
	}

	public void getUserTimeLine(String screenName,AsyncHttpResponseHandler handler) {
		String url = getApiUrl("statuses/user_timeline.json");
		Log.d(Constants.TAG,"URL for user timeline  == " + url + ", handle == " + screenName);
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		client.get(url,params,handler);
	}
}