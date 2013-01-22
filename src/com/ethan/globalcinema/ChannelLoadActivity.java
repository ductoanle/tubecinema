package com.ethan.globalcinema;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.globalcinema.loader.MovieInfoLoader;
import com.ethan.globalcinema.utils.MessengerUtils;
import com.omertron.themoviedbapi.model.MovieDb;

public class ChannelLoadActivity extends FragmentActivity implements LoaderCallbacks<MovieDb>, Handler.Callback{
	private static final String TAG = "ChannelActivity";

	public static final String MOVIE_ID_EXTRA = "movie_id";

	protected View loadingIndicator;
	protected View errorIndicator;
	protected TextView emptyListMessage;
	protected ImageView refreshButton;

	private int movieId;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_activity);
		initResources();
		loadMovie();
		callLoader(0, false, null);
	}

	private void initResources() {
		loadingIndicator = findViewById(R.id.loadingpagination_loading);
		errorIndicator = findViewById(R.id.loadingpagination_error);
		emptyListMessage = (TextView)findViewById(R.id.empty_message);
		refreshButton = (ImageView)findViewById(R.id.loadingpagination_error_refresh_btn);
		refreshButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				refreshLoadingPage();
			}
		});
	}

	private void refreshLoadingPage(){
		callLoader(0, true, null);
	}

	private void callLoader(int loaderId, boolean force, Bundle params){
		LoaderManager manager = getSupportLoaderManager();
		if (manager.getLoader(loaderId) == null){
			manager.initLoader(loaderId, params, this);
		}
	}

	private void loadMovie(){
		Intent intent = getIntent();
		if (intent.getExtras().containsKey(MOVIE_ID_EXTRA)){
			movieId = intent.getExtras().getInt(MOVIE_ID_EXTRA);
		}
		else {
			finish();
		}
	}

	public int getMovieId(){
		return movieId;
	}

	@Override
	public Loader<MovieDb> onCreateLoader(int loaderId, Bundle args) {
		return new MovieInfoLoader(this, movieId, new Messenger(new Handler(this)));
	}

	@Override
	public void onLoadFinished(Loader<MovieDb> loader, MovieDb movie) {
		if (movie != null){
			getSupportLoaderManager().destroyLoader(loader.getId());
			Intent intent = new Intent(this, ChannelActivity.class);
			intent.putExtra(ChannelActivity.MOVIE_EXTRA, (Parcelable)movie);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onLoaderReset(Loader<MovieDb> arg0) {
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MessengerUtils.UPDATE_DONE:
			errorIndicator.setVisibility(View.GONE);
			loadingIndicator.setVisibility(View.GONE);
			break;
		case MessengerUtils.UPDATE_ERROR:
			loadingIndicator.setVisibility(View.GONE);
			errorIndicator.setVisibility(View.VISIBLE);
			break;
		case MessengerUtils.UPDATE_WAIT:
			errorIndicator.setVisibility(View.GONE);
			loadingIndicator.setVisibility(View.VISIBLE);
			emptyListMessage.setVisibility(View.GONE);
			break;
		case MessengerUtils.UPDATE_EMPTY:
			errorIndicator.setVisibility(View.GONE);
			loadingIndicator.setVisibility(View.GONE);
			emptyListMessage.setVisibility(View.VISIBLE);
			break;
		default:
			return false;
		}
		return true;
	}
}

