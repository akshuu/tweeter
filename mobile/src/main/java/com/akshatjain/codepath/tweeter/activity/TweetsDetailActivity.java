package com.akshatjain.codepath.tweeter.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.akshatjain.codepath.tweeter.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TweetsDetailActivity extends AppCompatActivity {

    @BindView(R.id.txtName)
    public TextView txtName;

    @BindView(R.id.txtHandle)
    public TextView txtHandle;

    @BindView(R.id.txtTime)
    public TextView txtTime;

    @BindView(R.id.txtRetweetCnt)
    public TextView txtRetweetCnt;

    @BindView(R.id.txtLikeCnt)
    public TextView txtLikeCnt;

    @BindView(R.id.txtTweet)
    public TextView txtTweet;

    @BindView(R.id.imgTweet)
    public ImageView imgTweet;

    @BindView(R.id.imageView)
    public ImageView profilePic;

    @BindView(R.id.btnLike)
    public ImageButton btnLike;

    @BindView(R.id.btnRetweet)
    public ImageButton btnRetweet;

    @BindView(R.id.btnReply)
    public ImageButton btnReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets_detail);

        ButterKnife.bind(this);

        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("Tweet"));

        updateUI(tweet);
    }

    private void updateUI(Tweet tweet) {

        txtTime.setText(tweet.getCreated_at());
        txtName.setText(tweet.getUserDetails().getName());

        String formattedTweet = tweet.getText();
        if(tweet.getEntities() != null && tweet.getEntities().getUrls() != null && tweet.getEntities().getUrls().size() != 0) {
            String url = tweet.getEntities().getUrls().get(0).url;
            formattedTweet = Utils.ParseTweet(tweet.getText(),url);
        }else{
            formattedTweet = Utils.ParseTweet(tweet.getText(),null);
        }


        txtTweet.setText(Html.fromHtml(formattedTweet));
        txtTweet.setMovementMethod(LinkMovementMethod.getInstance());

        txtHandle.setText("@" + tweet.getUserDetails().getScreenName());


        if(tweet.isFavorite()){
            btnLike.setBackgroundResource(R.drawable.like_fav);
        }else{
            btnLike.setBackgroundResource(R.drawable.like);
        }

        if(tweet.isRetweeted()){
            btnRetweet.setBackgroundResource(R.drawable.retweeted);
        }else{
            btnRetweet.setBackgroundResource(R.drawable.retweet);
        }
        if(tweet.getFavoriteCount() > 0) {
            txtLikeCnt.setText(tweet.getFavoriteCount() + "");
            txtLikeCnt.setVisibility(View.VISIBLE);
        }else{
            txtLikeCnt.setVisibility(View.GONE);
        }

        if(tweet.getRetweet_count() > 0) {
            txtRetweetCnt.setVisibility(View.VISIBLE);
            txtRetweetCnt.setText(tweet.getRetweet_count() + "");
        }else{
            txtRetweetCnt.setVisibility(View.GONE);
        }

        if(tweet.getUserDetails().getProfileImageUrl() != null){
            Glide.with(this)
                    .load(tweet.getUserDetails().getProfileImageUrl())
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.twitter_logo)
                    .into(profilePic);
        }else{
            profilePic.setImageResource(R.drawable.twitter_logo);
        }

        if(tweet.getEntities() != null && tweet.getEntities().getMedias() != null){
            String url = tweet.getEntities().getMedias().get(0).getMediaUrl();
            Glide.with(this)
                    .load(url)
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgTweet);
        }

    }
}
