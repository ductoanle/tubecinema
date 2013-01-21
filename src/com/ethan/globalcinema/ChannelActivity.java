package com.ethan.globalcinema;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.ethan.globalcinema.fragments.ChannelInformationFragment;
import com.ethan.globalcinema.fragments.ChannelVideosFragment;

public class ChannelActivity extends BaseActionBarActivity {
	private static final String TAG = "ChannelActivity";
	
	private static final String CURRENT_SELECTED_TAB = TAG + ".selectedTab";
	public static final String MOVIE_ID_EXTRA = "movie_id";
	public static final String MOVIE_TITLE_EXTRA = "movie_title";
	
	private int movieId;
	private String movieTitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getSherlockActionBar();
		loadMovie();
		int position = 0;
		if (savedInstanceState != null) {
			position = savedInstanceState.getInt(CURRENT_SELECTED_TAB);
		}
		addTabs(position);
	}
	
	public void loadMovie(){
		Intent intent = getIntent();
		if (intent.getExtras().containsKey(MOVIE_ID_EXTRA) && intent.getExtras().containsKey(MOVIE_TITLE_EXTRA)){
			movieId = intent.getExtras().getInt(MOVIE_ID_EXTRA);
			movieTitle = intent.getExtras().getString(MOVIE_TITLE_EXTRA);
		}
		else {
			finish();
		}
	}
	
	public int getMovieId(){
		return movieId;
	}
	
	public String getMovieTitle(){
		return movieTitle;
	}
	
	public void addTabs(int position){
		Tab tab;
		tab = actionBar
				.newTab()
				.setText(R.string.tab_channel_information)
				.setTabListener(
						new TabListener<ChannelInformationFragment>(getString(R.string.tab_channel_information), ChannelInformationFragment.class));
		actionBar.addTab(tab);

		// Review
		tab = actionBar
				.newTab()
				.setText(R.string.tab_channel_videos)
				.setTabListener(
						new TabListener<ChannelVideosFragment>(getString(R.string.tab_channel_videos), ChannelVideosFragment.class));
		actionBar.addTab(tab);

		if (position != 0){
			actionBar.setSelectedNavigationItem(position);
		}
	}
}

