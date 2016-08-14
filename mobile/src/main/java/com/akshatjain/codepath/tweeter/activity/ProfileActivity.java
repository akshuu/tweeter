package com.akshatjain.codepath.tweeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.Model;
import com.akshatjain.codepath.tweeter.FragmentLifecycle;
import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.akshatjain.codepath.tweeter.data.User;
import com.akshatjain.codepath.tweeter.data.Users;
import com.akshatjain.codepath.tweeter.model.AuthUserModel;
import com.akshatjain.codepath.tweeter.model.UserModel;
import com.akshatjain.codepath.tweeter.restclienttemplate.RestApplication;
import com.akshatjain.codepath.tweeter.restclienttemplate.TwitterClient;
import com.akshatjain.codepath.tweeter.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.imageView2)
    ImageView profImage;

    @BindView(R.id.txtProfDesc)
    TextView description;

    @BindView(R.id.txtProfName)
    TextView name;

    @BindView(R.id.txtProfHandle)
    TextView handle;

    @BindView(R.id.txtProfFollowCnt)
    TextView followCnt;

    @BindView(R.id.txtProfFollowersCnt)
    TextView followerCnt;

    @BindView(R.id.txtProfFollow)
    TextView following;

    @BindView(R.id.txtProfFollowers)
    TextView followers;


    private TwitterClient twitterClient;
    String userHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        twitterClient = RestApplication.getRestClient();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userHandle = getIntent().getStringExtra("Username");
        Log.d(Constants.TAG,"Userhandle -" + userHandle);
        boolean isLoggedIn = getIntent().getBooleanExtra("isLoggedIn",false);
        UserModel userModel;

        if(!isLoggedIn) {
            final List<UserModel> lUserModel = UserModel.getUserByScreenName(userHandle);

            Log.d(Constants.TAG, "UserModel -" + lUserModel);
            userModel = lUserModel.get(0);
            if(userModel == null){
                Log.d(Constants.TAG, "Empty UserModel -" + userModel);

                return;
            }
        }else{
            userModel = AuthUserModel.getLoggedInUser();
        }
        name.setText(userModel.name);
        handle.setText("@" + userModel.screenName);
        description.setText(userModel.description);
        followCnt.setText(userModel.friends + "");
        followerCnt.setText(userModel.followers + "");

        Glide.with(this)
                .load(userModel.profileImageUrl)
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profImage);


        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterClient.getFriendsList(userHandle, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d(Constants.TAG, "Following Users == " + responseString);
                        Users lUser = (Users) new Gson()
                                .fromJson(responseString, Users.class);
                        Log.d(Constants.TAG, "Following Users: == " + lUser.users.size());
                        Intent followers = new Intent(ProfileActivity.this, FriendsFollowersActivity.class);
                        followers.putExtra("isFriendsList",true);
                        followers.putExtra("Users", Parcels.wrap(lUser));
                        startActivity(followers);
                    }
                });
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterClient.getFollowersList(userHandle, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d(Constants.TAG, "Followers Users == " + responseString);
                        Users lUser = (Users) new Gson()
                                .fromJson(responseString, Users.class);
                        Log.d(Constants.TAG, "Followers Users == " + lUser.users.size());

                        Intent followers = new Intent(ProfileActivity.this, FriendsFollowersActivity.class);
                        followers.putExtra("isFriendsList",false);
                        followers.putExtra("Users",Parcels.wrap(lUser));
                        startActivity(followers);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
