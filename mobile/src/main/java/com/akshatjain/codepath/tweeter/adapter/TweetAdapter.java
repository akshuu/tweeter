package com.akshatjain.codepath.tweeter.adapter;

/**
 * Created by akshatjain on 8/3/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akshatjain on 7/27/16.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.Holder>{


    private List<Tweet> tweetsList;
    private Context mContext;

    public TweetAdapter(List<Tweet> tweetsList, Context mContext) {
        this.tweetsList = tweetsList;
        this.mContext = mContext;
    }

    @Override
    public void onViewRecycled(Holder holder) {
        super.onViewRecycled(holder);
        // Required to clear image when the view is recycled
        // See  : https://github.com/bumptech/glide/issues/710
        Glide.clear(holder.profilePic);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View tweetView = inflater.inflate(R.layout.tweets, parent, false);

        // Return a new holder instance
        Holder viewHolder = new Holder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Tweet tweet = tweetsList.get(position);
        TextView txtUserName = holder.txtName;
        ImageView thumbnail = holder.profilePic;
        TextView txtHandle = holder.txtHandle;
        TextView txtRetweetCnt = holder.txtRetweetCnt;
        TextView txtLikeCnt = holder.txtLikeCnt;
        TextView txtTweet = holder.txtTweet;
        TextView txtTime = holder.txtTime;
        ImageView imgTweet = holder.imgTweet;

        txtUserName.setText(tweet.getUserDetails().getName());
        txtTweet.setText(tweet.getText());
        txtHandle.setText("@" + tweet.getUserDetails().getScreenName());


        if(tweet.isFavorite()){
            holder.btnLike.setBackgroundResource(R.drawable.like_fav);
        }else{
            holder.btnLike.setBackgroundResource(R.drawable.like);
        }

        if(tweet.isRetweeted()){
            holder.btnRetweet.setBackgroundResource(R.drawable.retweeted);
        }else{
            holder.btnRetweet.setBackgroundResource(R.drawable.retweet);
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
            Glide.with(mContext)
                    .load(tweet.getUserDetails().getProfileImageUrl())
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.twitter_logo)
                    .into(thumbnail);
        }else{
            thumbnail.setImageResource(R.drawable.twitter_logo);
        }

        holder.itemView.setTag(tweet);
    }

    @Override
    public int getItemCount() {
        return tweetsList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

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

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}