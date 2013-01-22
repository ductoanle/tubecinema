package com.ethan.globalcinema.beans;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class YoutubeVideo {
    private static final String TAG = "YoutubeVideo";
    
    private static final String MEDIA_GROUP_JSON = "media$group";
    private static final String MEDIA_THUMBNAILS_JSON = "media$thumbnail";
    private static final String MEDIA_THUMBNAILS_URL_JSON = "url";
    private static final String MEDIA_TITLES_JSON = "media$title";
    private static final String MEDIA_DURATION_JSON = "yt$duration";
    private static final String MEDIA_DURATION_SECOND_JSON = "seconds";
    private static final String MEDIA_VIDEO_ID_JSON = "yt$videoid";
    private static final String MEDIA_CONTENT_JSON = "$t";
    private static final String MEDIA_ACCESS_CONTROL_JSON = "yt$accessControl";
    private static final String MEDIA_ACCESS_ACTION_JSON = "action";
    private static final String MEDIA_PERMISSION_JSON = "permission";
    private static final String MEDIA_ACTION_SYNDICATE = "syndicate";
    private static final String MEDIA_ACTION_ALLOWED = "allowed";

    private String videoId;
    private String title;
    private int duration;
    private String thumbnail;
    private boolean isTrailer;
    private boolean isSyndicate;

    public YoutubeVideo(String videoId, String title, int duration, String thumbnail, boolean isSyndicate) {
        this.videoId = videoId;
        this.title = title;
        this.duration = duration;
        this.thumbnail = thumbnail;
        this.isSyndicate = isSyndicate;
    }
    
    public static YoutubeVideo getYoutubeVideoFromJSON(JSONObject jsonObject) {
        YoutubeVideo video = null;
        try {
            JSONArray actions = jsonObject.getJSONArray(MEDIA_ACCESS_CONTROL_JSON);
            JSONObject action;
            boolean isSyndicate = false;
            for (int i = 0; i < actions.length(); i++) {
                action = actions.getJSONObject(i);
                if (action.getString(MEDIA_ACCESS_ACTION_JSON).equals(MEDIA_ACTION_SYNDICATE) && action.getString(MEDIA_PERMISSION_JSON).equals(MEDIA_ACTION_ALLOWED)) {
                    isSyndicate = true;
                    break;
                }
            }
            jsonObject = jsonObject.getJSONObject(MEDIA_GROUP_JSON);
            String title = jsonObject.getJSONObject(MEDIA_TITLES_JSON).getString(MEDIA_CONTENT_JSON);
            int duration = jsonObject.getJSONObject(MEDIA_DURATION_JSON).getInt(MEDIA_DURATION_SECOND_JSON);
            String thumbnail = jsonObject.getJSONArray(MEDIA_THUMBNAILS_JSON).getJSONObject(0).getString(MEDIA_THUMBNAILS_URL_JSON);
            String videoId = jsonObject.getJSONObject(MEDIA_VIDEO_ID_JSON).getString(MEDIA_CONTENT_JSON);
            video = new YoutubeVideo(videoId, title, duration, thumbnail, isSyndicate);
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return video;
    }

    public String getVideoId() {
        return videoId;
    }

    public boolean isTrailer() {
        return isTrailer;
    }

    public void setTrailer(boolean isTrailer) {
        this.isTrailer = isTrailer;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    public void setIsSyndicate(boolean isSyndicate) {
        this.isSyndicate = isSyndicate;
    }
    
    public boolean isSyndicate() {
        return this.isSyndicate;
    }
}
