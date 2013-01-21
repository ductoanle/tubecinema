package com.ethan.globalcinema.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.globalcinema.ChannelActivity;
import com.ethan.globalcinema.R;
import com.ethan.globalcinema.beans.MovieItem;
import com.ethan.globalcinema.cache.SuperImageLoader;
import com.ethan.globalcinema.loader.MovieInfoLoader;

public class ChannelInformationFragment extends LoaderFragment implements LoaderCallbacks<MovieItem> {

	private static final String TAG = "ChannelInformationFragment";
	
	private MovieItem movie;
	private ImageView poster;
	private TextView title;
	private TextView originalTitle;
	private TextView languages;
	private TextView runtime;
	private TextView tagLine;
	private TextView genres;
	private TextView overview;
	private View movieInformation;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
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
	protected void initResources(View root) {
		loadingIndicator = root.findViewById(R.id.loadingpagination_loading);
        errorIndicator = root.findViewById(R.id.loadingpagination_error);
        emptyListMessage = (TextView)root.findViewById(R.id.empty_message);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.channel_information, container, false);
		initResources(root);
		movieInformation = (View)root.findViewById(R.id.movie_information);
		poster = (ImageView)root.findViewById(R.id.movie_poster);
		title = (TextView)root.findViewById(R.id.movie_title);
		originalTitle = (TextView)root.findViewById(R.id.movie_original_title);
		languages = (TextView)root.findViewById(R.id.movie_spoken_languages);
		runtime = (TextView)root.findViewById(R.id.movie_runtime);
		tagLine = (TextView)root.findViewById(R.id.movie_tag_line);
		genres = (TextView)root.findViewById(R.id.movie_genres);
		overview = (TextView)root.findViewById(R.id.movie_overview);
		return root;
    }
	
	private void renderInformation(){
		title.setText(movie.getTitle());
		if (!TextUtils.isEmpty(movie.getOriginalTitle())){
			originalTitle.setText(movie.getOriginalTitle());
		}
		else{
			originalTitle.setVisibility(View.GONE);
		}
		languages.setText(movie.getSpokenLanguagesListAsText());
		runtime.setText(movie.getRuntimeAsHourMinFormat());
		tagLine.setText(movie.getTagLine());
		genres.setText(movie.getGenresListAsText());
		overview.setText(movie.getOverview());
		SuperImageLoader.getInstance(getActivity()).loadImage(movie.getPosterFull(), poster, 0, null, 0);
		movieInformation.setVisibility(View.VISIBLE);
	}
	
	@Override
	public Loader<MovieItem> onCreateLoader(int loaderId, Bundle args) {
		return new MovieInfoLoader(getActivity(), ((ChannelActivity)getActivity()).getMovieId(), new Messenger(new Handler(this)));
	}

	@Override
	public void onLoadFinished(Loader<MovieItem> loader, MovieItem movie) {
		this.movie = movie;
		renderInformation();
		getLoaderManager().destroyLoader(loader.getId());
	}

	@Override
	public void onLoaderReset(Loader<MovieItem> arg0) {
	}
}
