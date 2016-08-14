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
import com.akshatjain.codepath.tweeter.model.MentionsModel;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by akshatjain on 8/9/16.
 */
public class MentionTab extends Fragment implements TweetAdapter.OnItemClickListener, FragmentLifecycle {

    @BindView(R.id.tweets)
    RecyclerView rvTweets;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayoutManager mLayoutManager;

    private TwitterClient twitterClient;
    int mPage =0 ;
    TweetAdapter mTweetAdapter;
    ArrayList<Tweet> mTweetList = new ArrayList<>();

    public MentionTab() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_mention_tab, container, false);
        ButterKnife.bind(this,view);

        setRetainInstance(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchMentionTweets(true);
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
                fetchMentionTweets(false);
            }
        });


        twitterClient = RestApplication.getRestClient();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        fetchMentionTweets(false);


    }

    private void fetchMentionTweets(final boolean isRefresh) {

        if(Utils.isNetworkAvailable(getActivity())) {

            long since_id = -1,max_id = -1;

            if(isRefresh){
                max_id = -1;
                since_id = MentionsModel.getSinceId();
                Log.d(Constants.TAG, "Mention Tab : SinceId == " + since_id);

            }else if(mPage > 0){
                since_id = -1;
                if(mTweetList != null){
                    Tweet last = mTweetList.get(mTweetList.size() -1);
                    max_id = last.id;
                }
//                max_id = MentionsModel.getMaxId();
                Log.d(Constants.TAG, "Mention Tab : max Id == " + max_id);
            }

            twitterClient.getMentionTweets(mPage, since_id,max_id,new TextHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, final String res) {
                    // called when response HTTP status is "200 OK"
                    Log.d(Constants.TAG, "Mention Tab Response string== " + res);
                    // Define tweet class to correspond to the JSON response returned
                    if(res != null && "[]".equals(res)) {
                        if (isRefresh)
                            swipeRefreshLayout.setRefreshing(false);

                        return;
                    }
                    Type collectionType = new TypeToken<List<Tweet>>() {
                    }.getType();
                    List<Tweet> lTweets = (List<Tweet>) new Gson()
                            .fromJson(res, collectionType);
                    Log.d(Constants.TAG, "Tweet == " + lTweets.size());
                    for (int i = 0; i < lTweets.size(); i++) {
                        Log.d(Constants.TAG, "Saving Tweets to DB == " + lTweets.get(i).toString());
                        Tweet tweet = lTweets.get(i);

                        MentionsModel mentionsModel = new MentionsModel(tweet.getId());
                        mentionsModel.save();

                    }
                    if (isRefresh) {
                        swipeRefreshLayout.setRefreshing(false);
                        int curSize = mTweetAdapter.getItemCount();
//                        mTweetList.clear();
//                        mTweetAdapter.notifyItemRangeRemoved(0, curSize);

                    }
                    mTweetList.addAll(lTweets);

                    Collections.sort(mTweetList, new Comparator<Tweet>() {
                        @Override
                        public int compare(Tweet lhs, Tweet rhs) {
                            if(lhs.getId()< rhs.getId())
                                return 1;
                            else if(lhs.getId() == rhs.getId())
                                return 0;
                            return -1;
                        }
                    });

                    if(mTweetAdapter == null) {
                        Log.d(Constants.TAG, "new Adapter == " + mTweetList.size());
                        mTweetAdapter = new TweetAdapter(mTweetList, getActivity());
                        mTweetAdapter.setOnItemClickListener(MentionTab.this);
                        rvTweets.setAdapter(mTweetAdapter);

                        mTweetAdapter.notifyItemInserted(0);
                        rvTweets.scrollToPosition(0);
                    }else{
                        int curSize = mTweetAdapter.getItemCount();
                        Log.d(Constants.TAG, "old Adapter == " + mTweetList.size());
                        Log.d(Constants.TAG, "updating items in range == " + curSize + ", " + lTweets.size());
                        if(isRefresh){
                            mTweetAdapter.notifyItemRangeInserted(0,lTweets.size());
                        }else {
                            mTweetAdapter.notifyItemRangeInserted(curSize, lTweets.size());
                        }
                    }

                    Log.d(Constants.TAG, "++++++Mention Twet Adapter == " + mTweetAdapter);

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

        List<TweetModel> tweetModels = MentionsModel.getAllMentionedTweets();
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
                Log.d(Constants.TAG,"Failed Updated tweet : res ==" + responseString);

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
//                mTweetAdapter.notifyItemRemoved(position);
                mTweetList.add(position,tweetUpdated);
                mTweetAdapter.notifyItemChanged(position);
//                if(tweet.isRetweeted()){
//                    tweet.isRetweeted =false;
//                }else{
//                    tweet.isRetweeted = true;
//                }

            }

        });

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        Log.d(Constants.TAG,"+++MentionTab, mAdapter= " + mTweetAdapter);
        if(mTweetAdapter != null){
            mTweetAdapter.setOnItemClickListener(MentionTab.this);
        }
        if(mTweetAdapter == null || mTweetAdapter.getItemCount() == 0)
            fetchMentionTweets(false);
    }
}
