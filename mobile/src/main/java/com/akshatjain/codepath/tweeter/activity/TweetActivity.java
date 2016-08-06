package com.akshatjain.codepath.tweeter.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.adapter.DividerItemDecoration;
import com.akshatjain.codepath.tweeter.adapter.SpacesItemDecoration;
import com.akshatjain.codepath.tweeter.adapter.TweetAdapter;
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.akshatjain.codepath.tweeter.restclienttemplate.RestApplication;
import com.akshatjain.codepath.tweeter.restclienttemplate.TwitterClient;
import com.akshatjain.codepath.tweeter.utils.Constants;
import com.akshatjain.codepath.tweeter.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import org.scribe.model.Token;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class TweetActivity extends AppCompatActivity {

    @BindView(R.id.tweets)
    RecyclerView rvTweets;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayoutManager mLayoutManager;

    private TwitterClient twitterClient;

    ArrayList<Tweet> mTweetList = new ArrayList<>();
    TweetAdapter mTweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Toast.makeText(getApplicationContext(),"Refresh",Toast.LENGTH_SHORT).show();
                fetchTweets(true);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvTweets.setHasFixedSize(true);
//        rvTweets.addItemDecoration(new SpacesItemDecoration(16));
        rvTweets.addItemDecoration(new DividerItemDecoration(this));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(mLayoutManager);

        twitterClient = RestApplication.getRestClient();

        fetchTweets(false);
    }

    private void fetchTweets(final boolean isRefresh) {

        if(Utils.isNetworkAvailable(this)) {

            twitterClient.getHomeTimeline(0, new TextHttpResponseHandler() {

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
                        Log.d(Constants.TAG, "Tweet == " + lTweets.get(i).toString());
                    }
                    if (isRefresh) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    int size = mTweetList.size();
                    mTweetList.addAll(lTweets);
                    if(mTweetAdapter == null) {
                        Log.d(Constants.TAG, "new Adapter == " + mTweetList.size());
                        mTweetAdapter = new TweetAdapter(mTweetList, TweetActivity.this);
                        rvTweets.setAdapter(mTweetAdapter);

                        mTweetAdapter.notifyItemInserted(0);
                        rvTweets.scrollToPosition(0);
                    }else{
                        int curSize = mTweetAdapter.getItemCount();
                        Log.d(Constants.TAG, "updating items in range == " + curSize + ", " + lTweets.size());
                        mTweetAdapter.notifyItemRangeInserted(curSize,lTweets.size());

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.w(Constants.TAG, " but callback was received" + res, t);
                    Toast.makeText(TweetActivity.this, "Error getting the list of tweets. Please try again...", Toast.LENGTH_LONG).show();

                    if (isRefresh) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

            });
        }else{
            Toast.makeText(this,"No Internet connection. Please try again...",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
