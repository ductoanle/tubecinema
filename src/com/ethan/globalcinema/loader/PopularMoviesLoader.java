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

public class PopularMoviesLoader extends AsyncTaskLoader<List<MovieItem>> {
	
	private static final String TAG = "MovieSearchLoader";
	
	private Messenger messenger;
	private int page;
	
	public PopularMoviesLoader(Context context, Messenger messenger, int page) {
		super(context);
		this.messenger = messenger;
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
			List<MovieItem> movies = TMDBApi.getInstance().getPopularMovieList(page);
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
