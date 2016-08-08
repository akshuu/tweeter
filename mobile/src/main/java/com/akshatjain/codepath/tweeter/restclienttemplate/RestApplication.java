package com.akshatjain.codepath.tweeter.restclienttemplate;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = RestApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class RestApplication extends MultiDexApplication{
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		RestApplication.context = this;
		ActiveAndroid.initialize(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, RestApplication.context);
	}
}