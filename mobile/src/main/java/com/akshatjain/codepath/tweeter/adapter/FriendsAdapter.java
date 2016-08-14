package com.akshatjain.codepath.tweeter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.akshatjain.codepath.tweeter.data.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akshatjain on 8/13/16.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.Holder> {


    public List<User> usersList;
    public Context mContext;

    public FriendsAdapter(List<User> usersList, Context mContext) {
        this.usersList = usersList;
        this.mContext = mContext;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View userView = inflater.inflate(R.layout.friends, parent, false);

        // Return a new holder instance
        Holder viewHolder = new Holder(userView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User usr = usersList.get(position);
        holder.txtUserName.setText(usr.name);
        holder.txtUserHandle.setText(usr.screenName);
        holder.txtUserDesc.setText(usr.description);
        Glide.with(mContext)
                .load(usr.getProfileImageUrl())
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.twitter_logo)
                .into(holder.profilePic);

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtUserName)
        public TextView txtUserName;

        @BindView(R.id.txtUserHandle)
        public TextView txtUserHandle;

        @BindView(R.id.imageView)
        public ImageView profilePic;

        @BindView(R.id.txtUserDesc)
        public TextView txtUserDesc;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
