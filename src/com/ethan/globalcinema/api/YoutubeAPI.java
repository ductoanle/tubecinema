package com.ethan.globalcinema.api;

import java.util.HashMap;

import org.json.JSONObject;

import android.util.Log;

import com.ethan.globalcinema.beans.YoutubeVideo;
import com.ethan.globalcinema.network.NetworkUtils;

public class YoutubeAPI {
    private static final String TAG = "YoutubeAPI";

    private static final String BASE_API = "https://gdata.youtube.com/feeds/api/videos";
    private static final String API_KEY = "AIzaSyDz81b7wFN62uuPJOSfAVc4I6WmkWauRic";

    private static final String QUERY = "q";
    private static final String VERSION = "v";
    private static final String DURATION = "duration";
    private static final String PAID_CONTENT = "paid-content";
    private static final String ALT = "alt";
    private static final String KEY = "key";
    private static final String DURATION_LONG = "long";
    private static final String ALT_JSON = "json";
    private static final String VERSION_TWO = "2";

    private static YoutubeAPI instance = null;

    public static YoutubeAPI getInstance() {
        if (instance == null) {
            instance = new YoutubeAPI();
        }
        return instance;
    }

    public YoutubeVideo getTrailer(String query) throws Exception{
        return queryForYouTubeVideo(query, true);
    }

    public YoutubeVideo getFullVideo(String query) throws Exception{
        return queryForYouTubeVideo(query, false);
    }

    private YoutubeVideo queryForYouTubeVideo(String query, boolean isSearchingTrailer) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(PAID_CONTENT, "false");
        params.put(KEY, API_KEY);
        params.put(ALT, ALT_JSON);
        params.put(VERSION, VERSION_TWO);
        if (isSearchingTrailer) {
            params.put(QUERY, query + " trailer"); 
        }
        else {
            params.put(DURATION, DURATION_LONG);
            params.put(QUERY, query + " movie");
        }
        
        String response = NetworkUtils.getResponse(BASE_API, null, NetworkUtils.METHOD_GET, params);
        
        Log.i(TAG, "Response is " + response);
        JSONObject jsonObject = new JSONObject(response);
        return YoutubeVideo.getYoutubeVideoFromJSON(jsonObject.getJSONObject("feed").getJSONArray("entry").getJSONObject(0));
    }
}
