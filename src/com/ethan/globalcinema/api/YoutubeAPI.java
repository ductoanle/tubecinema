package com.ethan.globalcinema.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
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
    private static final String DURATION_SHORT = "short";
    private static final String DURATION_MEDIUM = "medium";
    private static final String DURATION_ANY = "any";
    private static final String[] COMMON_WORDS = {"the", "a", "an", "and", "or"};

    private static final String ANAMATION_TYPE = "animation";

    private static final String ALT_JSON = "json";
    private static final String VERSION_TWO = "2";

    private static YoutubeAPI instance = null;

    public static YoutubeAPI getInstance() {
        if (instance == null) {
            instance = new YoutubeAPI();
        }
        return instance;
    }

    public YoutubeVideo getTrailer(String title, String year, String type) throws Exception{
        Log.i(TAG, "search query is " + title + " " + year + " " + type + " trailer");
        List<YoutubeVideo> videos = queryForYouTubeVideo(title + " " + year + " " + type + " trailer", DURATION_ANY);
        if (!videos.isEmpty()) {
            YoutubeVideo video = videos.get(0); 
            video.setTrailer(true);
            return video;
        }
        return null;
    }

    public YoutubeVideo getFullVideo(String title, String originalTitle, String year, String type, int duration) throws Exception{
        YoutubeVideo bestMatchedVideo = null;
        int bestScore = 0;
        Log.i(TAG, "search query is " + title + " " + year + " " + type + " movie");
        List<YoutubeVideo> videos = queryForYouTubeVideo(title + " " + year + " " + type + " movie", DURATION_LONG);
        int score;
        for (YoutubeVideo video: videos) {
            score = 0;
            if (video.getTitle().toLowerCase().contains(title.toLowerCase())) {
                score += StringUtils.split(title).length + 1;
            }
            else if (video.getTitle().toLowerCase().contains(originalTitle.toLowerCase())) {
                score += StringUtils.split(originalTitle).length + 1;
            }
            else {
                int titleScore = 0;
                int originalTitleScore = 0;
                List<String> words = getTestWords(title);
                for (String word: words) {
                    
                    if (video.getTitle().toLowerCase().contains(word)) {
                        Log.i(TAG, "+ with word " + word);
                        titleScore++;
                    }
                    else {
                        Log.i(TAG, "- with word " + word);
                        titleScore--;
                    }
                }
                words = getTestWords(originalTitle);
                for (String word: words) {
                    if (video.getTitle().toLowerCase().contains(word)) {
                        Log.i(TAG, "+ with word " + word);
                        originalTitleScore++;
                    }
                    else {
                        originalTitleScore--;
                        Log.i(TAG, "- with word " + word);
                    }
                }
                if (titleScore > originalTitleScore) {
                    score += titleScore;
                }
                else {
                    score += originalTitleScore;
                }
            }

            int durationInMinutes  = video.getDuration()/60;
            if (durationInMinutes > duration - 15 && durationInMinutes < duration + 15) {
                score += 10;
            }
            if (score >= 10 && score > bestScore) {
                bestMatchedVideo = video; 
            }
            Log.i(TAG, "Title is " + video.getTitle() + " score " + score);
        }
        return bestMatchedVideo;
    }

    private List<String> getTestWords(String str) {
        List<String> words = new ArrayList<String>();
        str = str.toLowerCase().replace(",", " ").replace("-", " ").replace("'", " ");
        String[] strs = StringUtils.split(str);
        for (String s: strs) {
            boolean isCommon = false;
            for (String commonWord : COMMON_WORDS) {
                if (s.equals(commonWord)) {
                    isCommon = true;
                    break;
                }
            }
            if (!isCommon) words.add(s);
        }
        return words; 
    }

    private List<YoutubeVideo> queryForYouTubeVideo(String query, String duration ) throws Exception {
        List<YoutubeVideo> videos = new ArrayList<YoutubeVideo>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(PAID_CONTENT, "false");
        params.put(KEY, API_KEY);
        params.put(ALT, ALT_JSON);
        params.put(VERSION, VERSION_TWO);
        params.put(QUERY, query);
        if (!duration.equals(DURATION_ANY)) {
            params.put(DURATION, DURATION_LONG);
        }

        String response = NetworkUtils.getResponse(BASE_API, null, NetworkUtils.METHOD_GET, params);

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONObject("feed").getJSONArray("entry");
        YoutubeVideo video;
        for (int i = 0; i < jsonArray.length(); i++) {
            video = YoutubeVideo.getYoutubeVideoFromJSON(jsonArray.getJSONObject(i));
            if (video != null && video.isSyndicate()) {
                videos.add(video);
            }
        }
        return videos;
    }
}
