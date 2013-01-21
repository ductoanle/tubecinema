package com.ethan.globalcinema;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.MenuItem;

public abstract class BaseActionBarActivity extends BaseActivity{
	
	protected ActionBar actionBar;
	
	protected ActionBar getSherlockActionBar(){
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bar_nav));
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		return actionBar;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	//////////Inner classes //////////

	public class TabListener<T extends Fragment>
	implements ActionBar.TabListener
	{
		private final String mTag;
		private final Class<T> mClass;
		private final Bundle mExtras;
		private Fragment mFragment;

		/**
		 * Constructor used each time a new tab is created.
		 *
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(
				String tag,
				Class<T> clz,
				Bundle extras)
		{
			FragmentManager manager = getSupportFragmentManager();

			mTag = tag;
			mClass = clz;
			mExtras = extras;
			mFragment = manager.findFragmentByTag(mTag);
			if (mFragment != null && !mFragment.isDetached()) {
				FragmentTransaction ft = manager.beginTransaction();
				ft.detach(mFragment);
				ft.commit();
			}
		}

		public TabListener(String tag, Class<T> clz) {
			this(tag, clz, null);
		}

		public Fragment getFragment() {
			FragmentManager manager = getSupportFragmentManager();
			return manager.findFragmentByTag(mTag);
		}

		protected String getTag() {
			return mTag;
		}

		////// ActionBar.TabListener implementation

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (mFragment == null) {
				mFragment = Fragment.instantiate(BaseActionBarActivity.this, mClass.getName(), mExtras);
				ft.add(R.id.fragment_main, mFragment, mTag);
			} else {
				ft.attach(mFragment);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				ft.detach(mFragment);
			}
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	}
}
