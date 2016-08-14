package com.akshatjain.codepath.tweeter.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.data.Users;
import com.akshatjain.codepath.tweeter.fragment.FollowingFragment;
import com.akshatjain.codepath.tweeter.fragment.FriendsList;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsFollowersActivity extends AppCompatActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_followers);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Users lUsers = Parcels.unwrap(getIntent().getParcelableExtra("Users"));
        FragmentManager fm = getSupportFragmentManager();
        if(getIntent() != null && getIntent().getBooleanExtra("isFriendsList",true)) {
            getSupportActionBar().setTitle("Following");
            Fragment fragment = new FriendsList();
            Bundle bundle = new Bundle();
            bundle.putParcelable("Users", Parcels.wrap(lUsers));
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.frndsContent, fragment).commit();
        }else{
            getSupportActionBar().setTitle("Followers");
            Fragment fragment = new FollowingFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("Users",Parcels.wrap(lUsers));
            fragment.setArguments(bundle);

            fm.beginTransaction().replace(R.id.frndsContent, fragment).commit();
        }

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
