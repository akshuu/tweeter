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
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.akshatjain.codepath.tweeter.restclienttemplate.RestApplication;
import com.akshatjain.codepath.tweeter.restclienttemplate.TwitterClient;
import com.akshatjain.codepath.tweeter.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.scribe.model.Token;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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

    Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        ButterKnife.bind(this);

        token = (Token) getIntent().getSerializableExtra("Token");
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
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvTweets.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(mLayoutManager);

        TwitterClient client = RestApplication.getRestClient();

        client.getHomeTimeline(0, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // called when response HTTP status is "200 OK"
                Log.d(Constants.TAG,"Response string== " + res);
                // Define tweet class to correspond to the JSON response returned
                Type collectionType = new TypeToken<List<Tweet>>(){}.getType();
                List<Tweet> lTweets = (List<Tweet>) new Gson()
                        .fromJson(res , collectionType);
                Log.d(Constants.TAG,"Tweet == " + lTweets.size());
                for(int i=0;i<lTweets.size();i++){
                    Log.d(Constants.TAG,"Tweet == "  + lTweets.get(i).toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.w(Constants.TAG, " but callback was received" +  res , t);

            }

        });
//        fetchTweets();
    }
/*
    private void fetchTweets() {

        Call<Tweet> responseCall = tweetsService.getHomeTweets();
        responseCall.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, retrofit2.Response<Tweet> response) {
//                progressBar.setVisibility(View.GONE);
                Log.d(Constants.TAG, "response == " + response.code());
                ArrayList<Article> tmpArticles = response.body().response.articles;

                int size = mArticles.size();
                mArticles.addAll(tmpArticles);
                if(mAdapter == null) {
                    Log.d("NYTIME", "new Adapter == " + mArticles.size());
                    mAdapter = new ArticleAdapter(mArticles, NewsActivity.this);
                    articlesView.setAdapter(mAdapter);

                    mAdapter.notifyItemInserted(0);
                    articlesView.scrollToPosition(0);
                }else{
                    int curSize = mAdapter.getItemCount();
                    Log.d("NYTIME", "updating items in range == " + curSize + ", " + tmpArticles.size());
                    mAdapter.notifyItemRangeInserted(curSize,tmpArticles.size());

                }
                mPage = page;
                Log.d("NYTIME", "mPage == " + mPage);

            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                Log.d(Constants.TAG, "onFailure : response == " + t.toString());
                Toast.makeText(TweetActivity.this,"Unable to fetch the Articles. Please try again...",Toast.LENGTH_LONG).show();
            }
        });
    }
*/

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
