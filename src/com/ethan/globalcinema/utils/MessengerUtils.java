package com.ethan.globalcinema.utils;

import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class MessengerUtils {
	private static final String TAG = "MessengerUtils";
	
	/// Request states
	static public final int UPDATE_DONE = 1;
	static public final int UPDATE_WAIT = 2;
	static public final int UPDATE_ERROR = 3;
	static public final int UPDATE_EMPTY = 4;
	
	public static void sendMessage(Messenger callback, int what){
		try{
			Message msg = Message.obtain();
			msg.what = what;
			callback.send(msg);
		}
		catch (Exception e){
			Log.e(TAG, e.getMessage(), e);
		}
	}
}
