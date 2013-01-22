package com.ethan.globalcinema.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.globalcinema.ChannelActivity;
import com.ethan.globalcinema.R;
import com.ethan.globalcinema.adapters.YoutubeVideoAdapter;
import com.ethan.globalcinema.beans.YoutubeVideo;
import com.ethan.globalcinema.loader.YoutubeSearchLoader;
import com.ethan.globalcinema.youtube.YoutubePlayerActivity;
import com.omertron.themoviedbapi.model.MovieDb;

public class ChannelVideosFragment extends LoaderFragment implements LoaderCallbacks<List<YoutubeVideo>>, OnItemClickListener {

    private YoutubeVideoAdapter mAdapter;
    private AdapterView<YoutubeVideoAdapter> mAdapterView;
    private ImageView refreshButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        callLoader(0, true, null);
    }
    
    @Override
    protected void initResources(View root) {
        loadingIndicator = root.findViewById(R.id.loadingpagination_loading);
        errorIndicator = root.findViewById(R.id.loadingpagination_error);
        emptyListMessage = (TextView)root.findViewById(R.id.empty_message);
    }
    
    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        callLoader(0, false, null);
    }

    private void refreshLoadingPage(){
        callLoader(0, true, null);
    }
    
    private void callLoader(int loaderId, boolean force, Bundle params){
        LoaderManager manager = getLoaderManager();
        if (manager.getLoader(loaderId) == null){
            manager.initLoader(loaderId, params, this);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.channel_videos_fragment, container, false);
        initResources(root);
        mAdapter = new YoutubeVideoAdapter(getActivity(), new ArrayList<YoutubeVideo>());
        mAdapterView = (AdapterView<YoutubeVideoAdapter>)root.findViewById(R.id.channel_video_list);
        mAdapterView.setVerticalFadingEdgeEnabled(true);
        mAdapterView.setAdapter(mAdapter);
        mAdapterView.setOnItemClickListener(this);
        
        refreshButton = (ImageView)root.findViewById(R.id.loadingpagination_error_refresh_btn);
        refreshButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                refreshLoadingPage();
            }
        });
        TextView loadingText = (TextView)root.findViewById(R.id.loading_text_msg);
        if (loadingText != null) {
            loadingText.setText(getActivity().getString(R.string.fetching_from_tmdb));
        }

        return root;
    }
    
    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        YoutubeVideo video = mAdapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra(YoutubePlayerActivity.VIDEO_ID, video.getVideoId());
        intent.setClass(getActivity(), YoutubePlayerActivity.class);
        startActivity(intent);
    }
    
    @Override
    public Loader<List<YoutubeVideo>> onCreateLoader(int loader, Bundle params) {
    	MovieDb movie = ((ChannelActivity)getActivity()).getMovie();
        return new YoutubeSearchLoader(getActivity(), movie.getTitle(), movie.getRuntime(), new Messenger(new Handler(this)));
    }
    
    @Override
    public void onLoadFinished(Loader<List<YoutubeVideo>> loader,
            List<YoutubeVideo> videos) {
        if (videos != null && !videos.isEmpty()) {
            mAdapter.setNotifyOnChange(false);
            mAdapter.clear();
            if (videos != null && videos.size() > 0){
                for(YoutubeVideo video:videos){
                    mAdapter.add(video);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
        getLoaderManager().destroyLoader(0);
    }

    @Override
    public void onLoaderReset(Loader<List<YoutubeVideo>> loader) {
        mAdapter.clear();
    }
}
