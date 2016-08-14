package com.akshatjain.codepath.tweeter.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akshatjain.codepath.tweeter.FragmentLifecycle;
import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.adapter.DividerItemDecoration;
import com.akshatjain.codepath.tweeter.adapter.EndlessRecyclerViewScrollListener;
import com.akshatjain.codepath.tweeter.adapter.TweetAdapter;
import com.akshatjain.codepath.tweeter.data.Entities;
import com.akshatjain.codepath.tweeter.data.Media;
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.akshatjain.codepath.tweeter.data.User;
import com.akshatjain.codepath.tweeter.model.EntitiesModel;
import com.akshatjain.codepath.tweeter.model.MediaModel;
import com.akshatjain.codepath.tweeter.model.TweetModel;
import com.akshatjain.codepath.tweeter.model.UserModel;
import com.akshatjain.codepath.tweeter.restclienttemplate.RestApplication;
import com.akshatjain.codepath.tweeter.restclienttemplate.TwitterClient;
import com.akshatjain.codepath.tweeter.utils.Constants;
import com.akshatjain.codepath.tweeter.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTweetFragment extends Fragment implements TweetAdapter.OnItemClickListener,FragmentLifecycle {


    public HomeTweetFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.tweets)
    RecyclerView rvTweets;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayoutManager mLayoutManager;

    private TwitterClient twitterClient;
    int mPage =0 ;
    TweetAdapter mTweetAdapter;
    ArrayList<Tweet> mTweetList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_home_tweet, container, false);
        ButterKnife.bind(this,view);

        setRetainInstance(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchHomeTweets(true);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvTweets.setHasFixedSize(true);
        rvTweets.addItemDecoration(new DividerItemDecoration(getActivity()));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(mLayoutManager);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d(Constants.TAG, "page ==" + page);
                mPage = page ;
                fetchHomeTweets(false);
            }
        });


        twitterClient = RestApplication.getRestClient();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchHomeTweets(false);

    }

    private void fetchHomeTweets(final boolean isRefresh) {

        if(Utils.isNetworkAvailable(getActivity())) {

            twitterClient.getHomeTimeline(mPage, new TextHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, String res) {
                    // called when response HTTP status is "200 OK"
                    Log.d(Constants.TAG, "Response string== " + res);
                    // Define tweet class to correspond to the JSON response returned
                    Type collectionType = new TypeToken<List<Tweet>>() {
                    }.getType();
                    List<Tweet> lTweets = (List<Tweet>) new Gson()
                            .fromJson(res, collectionType);
                    Log.d(Constants.TAG, "Tweet == " + lTweets.size());
                    for (int i = 0; i < lTweets.size(); i++) {
                        Log.d(Constants.TAG, "Saving Tweets to DB == " + lTweets.get(i).toString());
                        Tweet tweet = lTweets.get(i);
                        User user = tweet.getUserDetails();
                        UserModel userModel = new UserModel(user.getId(),
                                user.getName(),
                                user.getLikes(),
                                user.getScreenName(),
                                user.getDescription(),
                                user.getProfileImageUrl(),
                                user.followersCnt,
                                user.friendsCnt
                        );

                        userModel.save();

                        EntitiesModel entitiesModel = null;
                        Log.d(Constants.TAG,"SAving entities : " + tweet.getEntities());
                        if(tweet.getEntities() != null) {
                            List<Media> mediasList = tweet.getEntities().getMedias();
                            if (mediasList != null) {
                                for (Media media : mediasList) {
                                    MediaModel mediaModel = new MediaModel(media.getId(), media.getMediaUrl());
                                    mediaModel.save();
                                    entitiesModel = new EntitiesModel(mediaModel);
                                    Log.d(Constants.TAG,"calling save on entities : " + entitiesModel);
                                    entitiesModel.save();

                                }
                            }
                        }


                        TweetModel tweetModel = new TweetModel(tweet.getId(),tweet.getCreated_at(),tweet.getText(),
                                tweet.getRetweet_count(),tweet.isFavorite(),
                                tweet.isRetweeted(),tweet.getFavoriteCount(),userModel,entitiesModel );
                        tweetModel.save();

                    }
                    if (isRefresh) {
                        swipeRefreshLayout.setRefreshing(false);
                        int curSize = mTweetAdapter.getItemCount();
                        mTweetList.clear();
                        mTweetAdapter.notifyItemRangeRemoved(0, curSize);
                    }

                    mTweetList.addAll(lTweets);
                    if(mTweetAdapter == null) {
                        Log.d(Constants.TAG, "new Adapter == " + mTweetList.size());
                        mTweetAdapter = new TweetAdapter(mTweetList, getActivity());
                        mTweetAdapter.setOnItemClickListener(HomeTweetFragment.this);
                        rvTweets.setAdapter(mTweetAdapter);

                        mTweetAdapter.notifyItemInserted(0);
                        rvTweets.scrollToPosition(0);
                    }else{
                        int curSize = mTweetAdapter.getItemCount();
                        Log.d(Constants.TAG, "updating items in range == " + curSize + ", " + lTweets.size());
                        if(isRefresh){
                            mTweetAdapter.notifyItemRangeInserted(0,lTweets.size());
                        }else {
                            mTweetAdapter.notifyItemRangeInserted(curSize, lTweets.size());
                        }
                    }

                    Log.d(Constants.TAG, "++++++Home Twet Adapter == " + mTweetAdapter);


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.w(Constants.TAG, " but callback was received" + res, t);

                    if (isRefresh) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    loadOfflineTweets();
                }

            });
        }else{
            Snackbar.make(rvTweets, "No Internet connection. Please try again...", Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show();

            loadOfflineTweets();
            if (isRefresh) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    private void loadOfflineTweets() {
        Log.w(Constants.TAG, "loading offline tweets");
        List<Tweet> lTweets = new ArrayList<>();
        List<TweetModel> tweetModels = TweetModel.getAllTweets();
        for(TweetModel model : tweetModels){
            UserModel userModel = model.userModel;
            User user = new User(userModel.remoteId,
                    userModel.name,
                    userModel.likes,
                    userModel.screenName,
                    userModel.description,
                    userModel.profileImageUrl);

            EntitiesModel entitiesModel = model.entitiesModel;
            MediaModel mediaModel;

            Entities entities = null;
            if(entitiesModel != null){
                mediaModel = entitiesModel.mediaModel;
                Media media = new Media( mediaModel.remoteId,mediaModel.mediaURL,null);
                List<Media> lMedia = new ArrayList<>();
                lMedia.add(media);
                entities = new Entities(lMedia);
            }
            Tweet tweet = new Tweet(model.remoteId,
                    model.created_at,
                    model.text,
                    model.retweet_count,
                    model.isFavorite,
                    model.isRetweeted,
                    user,
                    model.favoriteCount,
                    entities
            );


            Log.w(Constants.TAG,"Saved tweet = " + tweet.toString());
            lTweets.add(tweet);
        }

        mTweetList = new ArrayList<>();

        Log.w(Constants.TAG,"MTAdapter = " + mTweetAdapter);
        if(mTweetAdapter != null) {
            int curSize = mTweetAdapter.getItemCount();
            mTweetAdapter.notifyItemRangeRemoved(0, curSize);
            mTweetList.addAll(lTweets);
        }else{
            mTweetList.addAll(lTweets);
            mTweetAdapter = new TweetAdapter(mTweetList, getActivity());
        }
        rvTweets.setAdapter(mTweetAdapter);
        Log.w(Constants.TAG, "total offline tweets : " + lTweets.size());

        mTweetAdapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }

    @Override
    public void onItemClick(int position) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ComposeFragment composeFragment = new ComposeFragment();
        Bundle arg = new Bundle();
        Tweet tweet = mTweetList.get(position);
        Log.d(Constants.TAG,"Handle == " + tweet.getUserDetails().getScreenName());
        arg.putString("Name",tweet.getUserDetails().getScreenName());
        arg.putLong("id",tweet.getId());
        composeFragment.setArguments(arg);
        composeFragment.show(fm, "composeFragment");
    }

    @Override
    public void onRetweet(final View v, final int position) {
        final Tweet tweet = mTweetList.get(position);
        long retweetId = tweet.getId();
        boolean bShouldRetweet = true;
        if(tweet.isRetweeted())
            bShouldRetweet = false;
        Log.d(Constants.TAG,"Handle == " + tweet.getUserDetails().getScreenName()  +" , retweet == " + retweetId);
        twitterClient.retweet(retweetId,bShouldRetweet,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Snackbar.make(v, "Unable to update the tweet. Please try again", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Snackbar.make(v, "Update the tweet.", Snackbar.LENGTH_LONG)
                        .setAction("Ok", null).show();
                Log.d(Constants.TAG,"Updated tweet : res ==" + responseString);
                Tweet tweetUpdated = (Tweet) new Gson()
                        .fromJson(responseString, Tweet.class);

                Log.d(Constants.TAG,"Updated tweet : tweet :  ==" + tweetUpdated.isRetweeted());
                mTweetList.remove(position);
                mTweetList.add(position,tweetUpdated);
                mTweetAdapter.notifyItemChanged(position);

            }

        });

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        Log.d(Constants.TAG,"+++HomeTweetTab, mAdapter= " + mTweetAdapter);
        if(mTweetAdapter != null){
            mTweetAdapter.setOnItemClickListener(HomeTweetFragment.this);

        }
        if(mTweetAdapter == null || mTweetAdapter.getItemCount() == 0)
            fetchHomeTweets(false);

    }
}
