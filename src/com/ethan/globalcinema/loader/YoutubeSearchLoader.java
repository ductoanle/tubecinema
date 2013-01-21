package com.ethan.globalcinema.loader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Messenger;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.ethan.globalcinema.api.YoutubeAPI;
import com.ethan.globalcinema.beans.YoutubeVideo;
import com.ethan.globalcinema.utils.MessengerUtils;
import com.ethan.globalcinema.utils.Utils;

public class YoutubeSearchLoader extends AsyncTaskLoader<List<YoutubeVideo>>{
    private static final String TAG = "YoutubeSearchLoader";
    
    private String query;
    private Messenger callback;
    
    public YoutubeSearchLoader(Context context, String query, Messenger callback) {
        super(context);
        this.query = query;
        this.callback = callback;
    }
    
    @Override
    protected void onStartLoading() {
        forceLoad();
        MessengerUtils.sendMessage(callback, MessengerUtils.UPDATE_WAIT);
    }

    @Override
    public List<YoutubeVideo> loadInBackground() {
        List<YoutubeVideo> videos = new ArrayList<YoutubeVideo>();
        try {
            YoutubeVideo video = YoutubeAPI.getInstance().getTrailer(query);
            if (video != null) {
                videos.add(video);
            }
            video = YoutubeAPI.getInstance().getFullVideo(query);
            if (video != null) {
                videos.add(video);
            }
            if (Utils.isListValid(videos)) {
                MessengerUtils.sendMessage(callback, MessengerUtils.UPDATE_DONE);
            }
            else {
                MessengerUtils.sendMessage(callback, MessengerUtils.UPDATE_EMPTY);
            }
            return videos;
        }
        catch(Exception e) {
            Log.i(TAG, e.getMessage(), e);
            MessengerUtils.sendMessage(callback, MessengerUtils.UPDATE_ERROR);
        }
        return videos;
    }
}
