package com.akshatjain.codepath.tweeter.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by akshatjain on 8/6/16.
 */
@Table(name="entities")
public class EntitiesModel  extends Model {

    @Column(name = "media", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public MediaModel mediaModel;

    public EntitiesModel() {
    }

    public EntitiesModel(MediaModel mediaModel) {
        this.mediaModel = mediaModel;
    }

    public List<TweetModel> tweets(){
        return getMany(TweetModel.class,"entities");
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EntitiesModel{");
        sb.append("mediaModel=").append(mediaModel);
        sb.append('}');
        return sb.toString();
    }
}
