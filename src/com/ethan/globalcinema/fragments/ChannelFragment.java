package com.ethan.globalcinema.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
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
import com.ethan.globalcinema.adapters.ChannelAdapter;
import com.ethan.globalcinema.beans.MovieItem;
import com.ethan.globalcinema.loader.PopularMoviesLoader;
import com.ethan.globalcinema.loader.SearchMoviesLoader;
import com.omertron.themoviedbapi.model.MovieDb;

public class ChannelFragment extends LoaderFragment implements LoaderCallbacks<List<MovieItem>>, OnItemClickListener{
	private static final String TAG = "SearchChannelFragment";
	
	private ChannelAdapter mAdapter;
    private AdapterView<ChannelAdapter> mAdapterView;
    private ImageView refreshButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
        if (manager.getLoader(loaderId) == null && mAdapter.getCount() == 0){
            manager.initLoader(loaderId, params, this);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.channel_fragment, container, false);
        initResources(root);
        mAdapter = new ChannelAdapter(getActivity(), new ArrayList<MovieItem>());
        mAdapterView = (AdapterView<ChannelAdapter>)root.findViewById(R.id.channel_list);
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
    	MovieItem movie = mAdapter.getItem(position);
    	Intent intent = new Intent();
    	intent.putExtra(ChannelActivity.MOVIE_ID_EXTRA, movie.getId());
    	intent.putExtra(ChannelActivity.MOVIE_TITLE_EXTRA, movie.getTitle());
    	intent.setClass(getActivity(), ChannelActivity.class);
    	startActivity(intent);
    }
    
    @Override
    public Loader<List<MovieItem>> onCreateLoader(int loader, Bundle params) {
        return new PopularMoviesLoader(getActivity(), new Messenger(new Handler(this)), 0);
    }
    
    @Override
    public void onLoadFinished(Loader<List<MovieItem>> loader,
            List<MovieItem> movies) {
        if (movies != null && !movies.isEmpty()) {
            mAdapter.setNotifyOnChange(false);
            mAdapter.clear();
            if (movies != null && movies.size() > 0){
                for(MovieItem movie:movies){
                    mAdapter.add(movie);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
        getLoaderManager().destroyLoader(0);
    }

    @Override
    public void onLoaderReset(Loader<List<MovieItem>> loader) {
        mAdapter.clear();
    }
}
