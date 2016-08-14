package com.akshatjain.codepath.tweeter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.akshatjain.codepath.tweeter.FragmentLifecycle;
import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.adapter.TweetAdapter;
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.akshatjain.codepath.tweeter.data.User;
import com.akshatjain.codepath.tweeter.fragment.ComposeFragment;
import com.akshatjain.codepath.tweeter.fragment.HomeTweetFragment;
import com.akshatjain.codepath.tweeter.fragment.MentionTab;
import com.akshatjain.codepath.tweeter.model.AuthUserModel;
import com.akshatjain.codepath.tweeter.restclienttemplate.RestApplication;
import com.akshatjain.codepath.tweeter.restclienttemplate.TwitterClient;
import com.akshatjain.codepath.tweeter.utils.Constants;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class TweetActivity extends AppCompatActivity implements ComposeFragment.OnTweetComposed,
        DataApi.DataListener{

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    /*
    @BindView(R.id.tweets)
    RecyclerView rvTweets;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayoutManager mLayoutManager;
    */
    private TwitterClient twitterClient;

    ArrayList<Tweet> mTweetList = new ArrayList<>();
    TweetAdapter mTweetAdapter;
    int mPage =0 ;
    GoogleApiClient mGoogleApiClient;
    private ViewPagerAdapter adapter;
    private Drawer navDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        DrawerBuilder drawerBuilder = new DrawerBuilder().withActivity(this);
        drawerBuilder.inflateMenu(R.menu.drawer_view);
        drawerBuilder.build();

//        createNavigationDrawer();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                showComposeFragment();


            }
        });

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        twitterClient = RestApplication.getRestClient();
        twitterClient.getUserProfile(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                User user = (User) new Gson()
                        .fromJson(responseString, User.class);

                Log.d(Constants.TAG,"Logged IN user == " + user.getScreenName());
                AuthUserModel model = new AuthUserModel(user.getId(),
                        user.getName(),
                        user.getLikes(),
                        user.getScreenName(),
                        user.getDescription(),
                        user.getProfileImageUrl(),
                        user.followersCnt,
                        user.friendsCnt);
                model.save();
                createNavigationDrawer();
            }
        });
        connectToWear();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(Constants.TAG,"FCM Token == " + token);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void createNavigationDrawer() {
        // Create the AccountHeader
        AccountHeader headerResult = null;
        final AuthUserModel userModel = AuthUserModel.getLoggedInUser();
        if(userModel != null) {

            DrawerImageLoader.init(new AbstractDrawerImageLoader() {
                @Override
                public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                    Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
                }

                @Override
                public void cancel(ImageView imageView) {
                    Glide.clear(imageView);
                }

                @Override
                public Drawable placeholder(Context ctx, String tag) {
                    //define different placeholders for different imageView targets
                    //default tags are accessible via the DrawerImageLoader.Tags
                    //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                    if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                        return DrawerUIUtils.getPlaceHolder(ctx);
                    } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                        return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                    } else if ("customUrlItem".equals(tag)) {
                        return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                    }

                    //we use the default one for
                    //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                    return super.placeholder(ctx, tag);
                }
            });

            DrawerImageLoader.getInstance().getImageLoader().placeholder(this, DrawerImageLoader.Tags.PROFILE.name());

            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.drawable.header)
                    .addProfiles(
                            new ProfileDrawerItem().withName(userModel.name).withEmail("@"+userModel.screenName).withIcon(userModel.profileImageUrl)
                    )
                    /*.withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            return false;
                        }
                    })*/
                    .build();
        }
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.profile);
        SecondaryDrawerItem item2 = (SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(2).withName(R.string.settings);

//Now create your drawer and pass the AccountHeader.Result
        // do something with the clicked item :D
        DrawerBuilder navDrawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem().withName(R.string.logout)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        Snackbar.make(view, "you clicked " + position, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        if(position == 1){
                            showProfileActivity(userModel.screenName);
                        }
                        return true;
                    }
                });

        if(headerResult != null) {
            navDrawerBuilder.withAccountHeader(headerResult);
        }

        navDrawer = navDrawerBuilder.build();

    }

    private void showProfileActivity(String name) {
        Intent profile = new Intent(this,ProfileActivity.class);
        profile.putExtra("Username",name);
        profile.putExtra("isLoggedIn",true);
        startActivity(profile);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeTweetFragment(), "Timeline");
        adapter.addFragment(new MentionTab(), "Mentions");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        int currentPosition = 0;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            FragmentLifecycle fragmentToShow = (FragmentLifecycle)adapter.getItem(position);
            fragmentToShow.onResumeFragment();

            FragmentLifecycle fragmentToHide = (FragmentLifecycle)adapter.getItem(currentPosition);
            fragmentToHide.onPauseFragment();

            currentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void connectToWear() {
        SharedPreferences prefs = getSharedPreferences("WearState",MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(Constants.TAG, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                        editor.putBoolean("Connected",true);
                        editor.apply();
                        Wearable.DataApi.addListener(mGoogleApiClient, TweetActivity.this);

                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(Constants.TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(Constants.TAG, "onConnectionFailed: " + result);
                        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
                            // The Wearable API is unavailable
                            editor.putBoolean("Connected",false);
                            editor.apply();
                        }
                    }
                })
                // Request access only to the Wearable API
                .addApiIfAvailable(Wearable.API)
                .build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    public void showComposeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = new ComposeFragment();
        composeFragment.show(fm, "composeFragment");
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

        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
           /* case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;*/
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onTweetPosted() {
//            fetchTweets(true);
    }
/*
    @Override
    public void onItemClick(View itemView, int position) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = new ComposeFragment();
        Bundle arg = new Bundle();
        Tweet tweet = mTweetList.get(position);
        Log.d(Constants.TAG,"Handle == " + tweet.getUserDetails().getScreenName());
        arg.putString("Name",tweet.getUserDetails().getScreenName());
        arg.putLong("id",tweet.getId());
        composeFragment.setArguments(arg);
        composeFragment.show(fm, "composeFragment");
    }*/

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/count") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
//                    updateCount(dataMap.getInt(COUNT_KEY));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
