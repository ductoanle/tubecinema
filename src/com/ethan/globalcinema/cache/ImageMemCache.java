package com.ethan.globalcinema.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

public class ImageMemCache extends LruCache<String, Bitmap> {
	private static final String TAG = "ImageMemCache";
	private static ImageMemCache instance = null;
	private static final Object instanceLock = new Object();
	private static final int MIN_HEAP_SIZE = 24;
	private static int mHeapSize;
	
	private ImageMemCache(int maxSizeBytes) {
		super(maxSizeBytes);
	}
	
	public static ImageMemCache getInstance(Context context){
		if (instance != null) return instance;
		mHeapSize = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		if (mHeapSize < MIN_HEAP_SIZE) return null;
		synchronized(instanceLock){
			instance = new ImageMemCache(getMaxMemSize(context));
			return instance;
		}
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		// can try value.getByteCount() if API 12 and above
		return value.getRowBytes() * value.getHeight();
	}
	
	private static int getMaxMemSize(Context context){
		final ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//		Log.i(TAG, "Max heap size " + am.getMemoryClass());
		return am.getMemoryClass() * 1024 * 1024 / 8;
	}
}
