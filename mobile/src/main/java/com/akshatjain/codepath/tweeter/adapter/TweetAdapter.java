package com.akshatjain.codepath.tweeter.adapter;

/**
 * Created by akshatjain on 8/3/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.bumptech.glide.Glide;

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
        Tweet tweets = tweetsList.get(position);
        TextView txtHeadline = holder.txtName;
        ImageView thumbnail = holder.profilePic;

//        txtHeadline.setText(tweets.headline.main);
//        MediaImage thumbnailImage = tweets.getThumbnail();
//        thumbnail.setImageDrawable(null);
//        if(thumbnailImage != null){
//            String imageUrl = Constants.NYTIMES_SITE_URL + thumbnailImage.url;
//            Glide.with(mContext)
//                    .load(imageUrl)
//                    .centerCrop()
//                    .dontAnimate()
//                    .override(300,300)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.news_icon)
//                    .into(profilePic);
//        }else{
//            thumbnail.setImageResource(R.drawable.news_icon);
//
//        }

        holder.itemView.setTag(tweets);
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

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}