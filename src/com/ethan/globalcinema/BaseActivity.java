package com.ethan.globalcinema;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class BaseActivity extends SherlockFragmentActivity{
	public static final String TAG = "BaseActivity";
	
	@Override 
	protected void onCreate(Bundle savedInstanceStates){
		super.onCreate(savedInstanceStates);
		setContentView(R.layout.activity_main);
	}
}
