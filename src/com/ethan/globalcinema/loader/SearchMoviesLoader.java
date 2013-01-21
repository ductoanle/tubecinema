package com.ethan.globalcinema.loader;

import java.util.List;

import android.content.Context;
import android.os.Messenger;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.ethan.globalcinema.api.TMDBApi;
import com.ethan.globalcinema.beans.MovieItem;
import com.ethan.globalcinema.utils.MessengerUtils;
import com.omertron.themoviedbapi.MovieDbException;

public class SearchMoviesLoader extends AsyncTaskLoader<List<MovieItem>> {
	
	private static final String TAG = "MovieSearchLoader";
	
	private Messenger messenger;
	private String searchTitle;
	private String page;
	
	public SearchMoviesLoader(Context context, Messenger messenger, String searchTitle, String page) {
		super(context);
		this.messenger = messenger;
		this.searchTitle = searchTitle;
		this.page = page;
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
		MessengerUtils.sendMessage(messenger, MessengerUtils.UPDATE_WAIT);
	}
	
	@Override
	public List<MovieItem> loadInBackground() {
		try{
			List<MovieItem> movies = TMDBApi.getInstance().searchMovies(searchTitle, page);
			if (movies == null || movies.isEmpty()){
				MessengerUtils.sendMessage(messenger, MessengerUtils.UPDATE_EMPTY);
			}
			else{
				MessengerUtils.sendMessage(messenger, MessengerUtils.UPDATE_DONE);
			}
			return movies;
		}
		catch (MovieDbException mdbe){
			Log.e(TAG, mdbe.getMessage());
			MessengerUtils.sendMessage(messenger, MessengerUtils.UPDATE_ERROR);
		}
		return null;
	}
}
