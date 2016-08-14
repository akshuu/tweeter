package com.akshatjain.codepath.tweeter.adapter;

/**
 * Created by akshatjain on 8/3/16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.activity.TweetsDetailActivity;
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.akshatjain.codepath.tweeter.utils.Constants;
import com.akshatjain.codepath.tweeter.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akshatjain on 7/27/16.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.Holder>{


    public List<Tweet> tweetsList;
    public Context mContext;

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
        Glide.clear(holder.imgTweet);
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

        txtTime.setText(Utils.getRelativeTimeAgo(tweet.getCreated_at()));
        txtUserName.setText(tweet.getUserDetails().getName());

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

        if(tweet.getEntities() != null && tweet.getEntities().getMedias() != null){
            String url = tweet.getEntities().getMedias().get(0).getMediaUrl();
            Glide.with(mContext)
                    .load(url)
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgTweet);
        }

        holder.itemView.setTag(tweet);
    }

    @Override
    public int getItemCount() {
        return tweetsList.size();
    }

    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onRetweet(View v,int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        Log.d(Constants.TAG,"++++++ ItemClickListener == " + listener);
        this.listener = listener;
    }

    public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        public Holder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(getLayoutPosition());
                }
            });

            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onRetweet(v,getLayoutPosition());
//                    Tweet tweet = (Tweet) v.getTag();
//                    if(tweet.isRetweeted()){
//                        btnRetweet.setBackgroundResource(R.drawable.retweet);
//                    }else{
//                        btnRetweet.setBackgroundResource(R.drawable.retweeted);
//                    }
                }
            });
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Tweet tweet = (Tweet) v.getTag();
            if(tweet.getEntities() != null && tweet.getEntities().getUrls() != null && tweet.getEntities().getUrls().size() > 0)
            {
//                String url = tweet.getEntities().getUrls().get(0).url;
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                v.getContext().startActivity(i);
//                Log.d(Constants.TAG," URL with this == " + url);
                Intent intent = new Intent(v.getContext(), TweetsDetailActivity.class);
                intent.putExtra("Tweet", Parcels.wrap(tweet));
                v.getContext().startActivity(intent);
            }else{
                Log.d(Constants.TAG,"No URL with this ");
            }
        }
    }
}