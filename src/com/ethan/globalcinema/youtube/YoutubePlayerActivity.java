package com.ethan.globalcinema.youtube;

import android.os.Bundle;

import com.ethan.globalcinema.R;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayerActivity extends YouTubeFailureRecoveryActivity{

    private static final String TAG = "YoutubePlayerActivity";
    public static final String VIDEO_ID = "video_id";

    private String videoId;
    private YouTubePlayer player;
    private MyPlayerStateChangeListener playerStateChangeListener = new MyPlayerStateChangeListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_view);

        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);

        getVideoId();
    }

    private void getVideoId() {
        videoId = getIntent().getExtras().getString(VIDEO_ID);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
            boolean wasRestored) {
        this.player = player;
        this.player.setPlayerStateChangeListener(playerStateChangeListener);
        if (!wasRestored) {
            this.player.cueVideo(videoId);
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    private final class MyPlayerStateChangeListener implements PlayerStateChangeListener {
        String playerState = "UNINITIALIZED";

        @Override
        public void onLoading() {
            playerState = "LOADING";
        }

        @Override
        public void onLoaded(String videoId) {
            playerState = String.format("LOADED %s", videoId);
            player.play();
            player.setShowFullscreenButton(false);
        }

        @Override
        public void onAdStarted() {
            playerState = "AD_STARTED";
        }

        @Override
        public void onVideoStarted() {
            playerState = "VIDEO_STARTED";
        }

        @Override
        public void onVideoEnded() {
            playerState = "VIDEO_ENDED";
        }

        @Override
        public void onError(ErrorReason reason) {
            playerState = "ERROR (" + reason + ")";
            if (reason == ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) {
                // When this error occurs the player is released and can no longer be used.
                player = null;
            }
        }
    }

}
