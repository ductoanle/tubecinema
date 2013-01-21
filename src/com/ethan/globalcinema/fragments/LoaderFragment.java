package com.ethan.globalcinema.fragments;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.ethan.globalcinema.utils.MessengerUtils;

public abstract class LoaderFragment extends BaseFragment implements Handler.Callback {
	private static final String TAG = "LoaderFragment";

	protected View loadingIndicator;
	protected View errorIndicator;
	protected TextView emptyListMessage;
	
	protected abstract void initResources(View root);

	@Override
	public boolean handleMessage(Message msg) {
		Activity activity = getActivity();
		if (activity == null) return false;
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
