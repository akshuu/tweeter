package com.akshatjain.codepath.tweeter.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.activeandroid.query.Sqlable;
import com.akshatjain.codepath.tweeter.data.Tweet;
import com.akshatjain.codepath.tweeter.utils.Constants;

import java.util.List;

/**
 * Created by akshatjain on 8/10/16.
 */
@Table(name = "MentionsModel")
public class MentionsModel extends Model{

    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;

    public MentionsModel() {
        super();
    }

    public MentionsModel(long id) {
        this.remoteId = id;
    }

    public static List<TweetModel> getAllMentionedTweets() {
        // This is how you execute a query
        return new Select()
                .from(TweetModel.class)
                .innerJoin(MentionsModel.class)
                .on("MentionsModel.remote_id = TweetModel.remote_id")
                .execute();
    }

    public static long getMaxId(){
         From qury = new Select()
                .from(MentionsModel.class)
                .orderBy("remote_id ASC");
        List<MentionsModel> model = qury.execute();
        Log.d(Constants.TAG,"+++++ max Id query == " + qury.toSql());

        return (model == null|| model.size() == 0) ? -1 : model.get(0).remoteId;
    }

    public static long getSinceId(){
        From qury  = new Select()
                .from(MentionsModel.class)
                .orderBy("remote_id DESC");
        Log.d(Constants.TAG,"+++++ since Id query == " + qury.toSql());

        List<MentionsModel> model = qury.execute();
        Log.d(Constants.TAG,"+++++ since Id model size == " + model.size());
        return (model == null || model.size() == 0) ? -1 : model.get(0).remoteId;
    }
}
