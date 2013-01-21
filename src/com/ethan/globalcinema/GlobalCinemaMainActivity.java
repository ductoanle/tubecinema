package com.ethan.globalcinema;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.view.WindowManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.ethan.globalcinema.fragments.ChannelFragment;

public class GlobalCinemaMainActivity extends BaseActionBarActivity {
	
	private static final String TAG = "GlobalCinemaMainActivity";
	
	private static final String CURRENT_SELECTED_TAB = TAG + ".selectedTab";
	
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			Field field = WindowManager.LayoutParams.class.getField(
					"FLAG_HARDWARE_ACCELERATED");
			int flag = field.getInt(null);
			getWindow().setFlags(flag, flag);
			//Log.i(TAG, "Hardware acceleration enabled");
		} catch (Exception e) {
			//Log.i(TAG, "No hardware acceleration", e);
		}
		mActionBar = getSherlockActionBar();
		int position = 0;
		if (savedInstanceState != null) {
			position = savedInstanceState.getInt(CURRENT_SELECTED_TAB);
		}
		addTabs(position);
	}

	public void addTabs(int position){
		Tab tab;
		tab = mActionBar
				.newTab()
				.setText(R.string.popular_movies)
				.setTabListener(new TabListener<ChannelFragment>(getString(R.string.popular_movies), ChannelFragment.class));
				mActionBar.addTab(tab);

		mActionBar.addTab(tab);
	}
}
