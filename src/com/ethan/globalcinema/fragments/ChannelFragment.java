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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.globalcinema.ChannelLoadActivity;
import com.ethan.globalcinema.R;
import com.ethan.globalcinema.adapters.ChannelAdapter;
import com.ethan.globalcinema.loader.PopularMoviesLoader;
import com.omertron.themoviedbapi.model.MovieDb;

public class ChannelFragment extends LoaderFragment implements LoaderCallbacks<List<MovieDb>>, OnItemClickListener{
	private static final String TAG = "SearchChannelFragment";
	public static final String PAGE = "page";
    private static final String FORCE = "force";
    private static final String INSTANCE_STATE_CURRENT_PAGE = TAG + ".currentPage";
    private static final String INSTANCE_STATE_PREVIOUS_TOTAL = TAG + ".previousTotal";
    private static final String INSTANCE_STATE_LOADING = TAG + ".loading";
    private static final int PER_PAGE = 15;
	
	private ChannelAdapter mAdapter;
    private AdapterView mAdapterView;
    private ImageView refreshButton;
    private Bundle mBundle;
    private EndlessScrollListener mScrollListener;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            int currentPage = savedInstanceState.getInt(INSTANCE_STATE_CURRENT_PAGE, EndlessScrollListener.FIRST_PAGE);
            int previousTotal = savedInstanceState.getInt(INSTANCE_STATE_PREVIOUS_TOTAL, EndlessScrollListener.FIRST_PREVIOUS_TOTAL);
            boolean loading = savedInstanceState.getBoolean(INSTANCE_STATE_LOADING, false);
            mScrollListener = new EndlessScrollListener(currentPage, previousTotal, loading);
        }
        else {
            mScrollListener = new EndlessScrollListener();
        }
        mBundle = new Bundle();
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    if (mScrollListener != null){
            outState.putInt(INSTANCE_STATE_CURRENT_PAGE, mScrollListener.getCurrentPage());
            outState.putInt(INSTANCE_STATE_PREVIOUS_TOTAL, mScrollListener.getPreviousTotal());
            outState.putBoolean(INSTANCE_STATE_LOADING, mScrollListener.getLoading());
        }
	}
	
	@Override
	protected void initResources(View root) {
		loadingIndicator = root.findViewById(R.id.loadingpagination_loading);
        errorIndicator = root.findViewById(R.id.loadingpagination_error);
        emptyListMessage = (TextView)root.findViewById(R.id.empty_message);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    if (mScrollListener.getPreviousTotal() == (mScrollListener.getCurrentPage() - 1) * PER_PAGE){
            restartLoader(mScrollListener.getCurrentPage());
        }
	}
    
    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        mBundle.putString(PAGE, "1");
        callLoader(0, mBundle, this);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        destroyLoader();
    }
    
    private void restartLoader(int page){
        // Start loader
        mBundle.putString(PAGE, "" + page);
        callLoader(0, mBundle, this);
    }
    
    private void destroyLoader(){
        getLoaderManager().destroyLoader(0);
    }
    
    public void refreshLoadingPage(){
        restartLoader(mScrollListener.getCurrentPage());
        mScrollListener.setLoading(true);
    }

    public void refresh(boolean force) {
        mAdapter.clear();
        mBundle.putString(PAGE, "1");
        mBundle.putBoolean(FORCE, force);
        callLoader(0, mBundle, this);
        mScrollListener.reset();
        mAdapterView.setVisibility(View.GONE);
    }
    
    private void callLoader(int id, Bundle bundle, LoaderCallbacks<List<MovieDb>> callback){
        LoaderManager manager = getLoaderManager();
        if (manager.getLoader(id) != null){
            manager.restartLoader(id, bundle, callback);
        }
        else{
            manager.initLoader(id, bundle, callback);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.channel_fragment, container, false);
        initResources(root);
        mAdapter = new ChannelAdapter(getActivity(), new ArrayList<MovieDb>());
        mAdapterView = (AdapterView<ChannelAdapter>)root.findViewById(R.id.channel_list);
        mAdapterView.setVerticalFadingEdgeEnabled(true);
        mAdapterView.setAdapter(mAdapter);
        mAdapterView.setOnItemClickListener(this);
        ((GridView)mAdapterView).setOnScrollListener(mScrollListener);
        
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
    	MovieDb movie = mAdapter.getItem(position);
    	Intent intent = new Intent();
    	intent.putExtra(ChannelLoadActivity.MOVIE_ID_EXTRA, movie.getId());
    	intent.setClass(getActivity(), ChannelLoadActivity.class);
    	startActivity(intent);
    }
    
    @Override
    public Loader<List<MovieDb>> onCreateLoader(int loader, Bundle params) {
        return new PopularMoviesLoader(getActivity(), new Messenger(new Handler(this)), params.getString(PAGE));
    }
    
    @Override
    public void onLoadFinished(Loader<List<MovieDb>> loader,
            List<MovieDb> movies) {
        if (movies != null && !movies.isEmpty()) {
            mAdapter.setNotifyOnChange(false);
            for(MovieDb movie:movies){
                mAdapter.add(movie);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MovieDb>> loader) {
    }
    
    class EndlessScrollListener implements OnScrollListener {
        public static final int FIRST_PAGE = 1;
        private static final int VISIBLE_THRESHOLD = 5;
        public static final int FIRST_PREVIOUS_TOTAL = 0;

        private int currentPage;
        private int previousTotal;
        private boolean loading = true;

        public EndlessScrollListener(){
            super();
            this.currentPage = FIRST_PAGE;
            this.previousTotal = FIRST_PREVIOUS_TOTAL;
        }

        public EndlessScrollListener(int currentPage, int previousTotal, boolean loading){
            this.currentPage = currentPage;
            this.previousTotal = previousTotal;
            this.loading = loading;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD) && totalItemCount != 0) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                currentPage++;
                restartLoader(currentPage);
                loading = true;
            }
        }

        public void reset(){
            this.currentPage = FIRST_PAGE;
            this.previousTotal = FIRST_PREVIOUS_TOTAL;
            this.loading = true;
        }

        public int getCurrentPage(){
            return currentPage;
        }

        public int getPreviousTotal(){
            return previousTotal;
        }

        public boolean getLoading(){
            return loading;
        }

        public void setLoading(boolean loading){
            this.loading = loading;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }
}
