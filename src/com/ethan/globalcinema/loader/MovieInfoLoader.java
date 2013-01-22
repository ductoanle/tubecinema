package com.ethan.globalcinema.loader;

import android.content.Context;
import android.os.Messenger;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.ethan.globalcinema.api.TMDBApi;
import com.ethan.globalcinema.utils.MessengerUtils;
import com.omertron.themoviedbapi.model.MovieDb;

public class MovieInfoLoader extends AsyncTaskLoader<MovieDb> {
	
	private static final String TAG = "MovieInfoLoader";
	
	private int movieId;
	private Messenger callback;
	
	public MovieInfoLoader(Context context, int movieId, Messenger callback){
		super(context);
		this.movieId = movieId;
		this.callback = callback;
	}
	
	@Override
	protected void onStartLoading() {
		forceLoad();
		MessengerUtils.sendMessage(callback, MessengerUtils.UPDATE_WAIT);
	}

	@Override
	public MovieDb loadInBackground() {
		try{
			MovieDb movie = TMDBApi.getInstance().getMovieInfo(movieId);
			if (movie != null){
				MessengerUtils.sendMessage(callback, MessengerUtils.UPDATE_DONE);
			}
			else{
				MessengerUtils.sendMessage(callback, MessengerUtils.UPDATE_EMPTY);
			}
			return movie;
		}
		catch(Exception e){
			Log.e(TAG, e.getMessage(), e);
			MessengerUtils.sendMessage(callback, MessengerUtils.UPDATE_ERROR);
		}
		return null;
	}
}
