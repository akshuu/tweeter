package com.akshatjain.codepath.tweeter.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.activity.ProfileActivity;
import com.akshatjain.codepath.tweeter.adapter.DividerItemDecoration;
import com.akshatjain.codepath.tweeter.adapter.EndlessRecyclerViewScrollListener;
import com.akshatjain.codepath.tweeter.adapter.FriendsAdapter;
import com.akshatjain.codepath.tweeter.adapter.FriendsAdapter.OnItemClickListener;
import com.akshatjain.codepath.tweeter.data.User;
import com.akshatjain.codepath.tweeter.data.Users;
import com.akshatjain.codepath.tweeter.model.AuthUserModel;
import com.akshatjain.codepath.tweeter.model.UserModel;
import com.akshatjain.codepath.tweeter.restclienttemplate.RestApplication;
import com.akshatjain.codepath.tweeter.restclienttemplate.TwitterClient;
import com.akshatjain.codepath.tweeter.utils.Constants;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsList extends Fragment  implements OnItemClickListener{


    @BindView(R.id.users)
    RecyclerView rvUsers;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayoutManager mLayoutManager;

    private TwitterClient twitterClient;
    int mPage =0 ;
    FriendsAdapter mFriendsAdapter;
    ArrayList<User> mUsrs = new ArrayList<>();


    public FriendsList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        ButterKnife.bind(this,view);

        setRetainInstance(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
//                Toast.makeText(getActivity(),"Refresh",Toast.LENGTH_SHORT).show();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvUsers.setHasFixedSize(true);
        rvUsers.addItemDecoration(new DividerItemDecoration(getActivity()));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvUsers.setLayoutManager(mLayoutManager);

        rvUsers.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d(Constants.TAG, "page ==" + page);
                mPage = page ;
            }
        });
        twitterClient = RestApplication.getRestClient();

        Bundle bundle = getArguments();
        Users lusers = Parcels.unwrap(bundle.getParcelable("Users"));
        mFriendsAdapter = new FriendsAdapter(lusers.users,getActivity());
        rvUsers.setAdapter(mFriendsAdapter);
        mFriendsAdapter.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void showUserProfile(final String screenHandle) {

        Log.d(Constants.TAG,"user handle == " +screenHandle);

        twitterClient.getUser(screenHandle, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(Constants.TAG,"URL detail response == " + responseString);
                User user = (User) new Gson()
                        .fromJson(responseString, User.class);

                Log.d(Constants.TAG,"Logged IN user == " + user.getScreenName());

                Intent profile = new Intent(getActivity(),ProfileActivity.class);
                profile.putExtra("Username",screenHandle);
                profile.putExtra("User",Parcels.wrap(user));
                profile.putExtra("isLoggedIn",false);
                getActivity().startActivity(profile);
            }
        });



    }
}
