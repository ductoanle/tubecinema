package com.ethan.globalcinema.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ethan.globalcinema.utils.KeyGenerationUtils;

public class SuperImageLoader {
	private static final String TAG = "SuperImageLoader";
	private Context mContext;

	private static SuperImageLoader instance = null;
	private static final Object instanceLock = new Object();
	
	private SuperImageLoader(Context context){
		this.mContext = context.getApplicationContext();
	}

	public void loadImage(String url, ImageView target, int holder, ScaleType scaleType, int maxDuration){
		// Cancel any pending thumbnail task, since this view is now bound
		// to new thumbnail
		try{
			final ImageAsyncTask oldTask = (ImageAsyncTask) target.getTag();
			if (oldTask != null) {
				oldTask.cancel(false);
			}
			// Cache enabled, try looking for cache hit
			ImageAsyncTask task = new ImageAsyncTask(url, target, scaleType, maxDuration);
			// Clear existing image in the ImageView
			if (holder != 0){
				target.setImageDrawable(mContext.getResources().getDrawable(holder));
			}
			target.setTag(task);
			task.execute();
		}
		catch(Exception e){
			Log.e(TAG, e.getMessage(),e);
		}
	}

	public static SuperImageLoader getInstance(Context context){
		if (instance != null) return instance;
		synchronized(instanceLock){
			instance = new SuperImageLoader(context);
			return instance;
		}
	}

	public class ImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {
		private final ImageView mTarget;
		private String mUrl;
		private String mKey;
		private ScaleType mScaleType;
		private int maxDuration;

		public ImageAsyncTask(String url, ImageView target, ScaleType scaleType, int maxDuration) {
			mTarget = target;
			mUrl = url;
			mScaleType = scaleType;
			mKey = KeyGenerationUtils.generateImageKey(url);
			this.maxDuration = maxDuration; 
		}

		@Override
		protected void onPreExecute() {
			mTarget.setTag(this);
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap cachedResult = null;
			if (mUrl != null){
				String key = KeyGenerationUtils.generateImageKey(mUrl);
				if (ImageMemCache.getInstance(mContext) != null){
					cachedResult = ImageMemCache.getInstance(mContext).get(key);
					if (cachedResult != null){
						return cachedResult;
					}
				}
				cachedResult = ImageCache.getInstance(mContext).getImageFromCache(key);
				if (cachedResult != null){
					return cachedResult;
				}
				if (ImageCache.getInstance(mContext) != null){
					cachedResult = ImageCache.getInstance(mContext).saveImageToCache(mKey, mUrl, maxDuration);
				}

				// When cache enabled, keep reference to this bitmap
				if (ImageMemCache.getInstance(mContext) != null && cachedResult != null){
					ImageMemCache.getInstance(mContext).put(mKey, cachedResult);
				}
			}
			return cachedResult;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (mTarget.getTag() == this) {
				if (mScaleType != null){
					mTarget.setScaleType(mScaleType);
				}
				if (result != null){
					mTarget.setImageBitmap(result);
				}
				mTarget.setTag(null);
			}
		}        
	}
}
