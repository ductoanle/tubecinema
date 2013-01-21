package com.ethan.globalcinema.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ethan.globalcinema.utils.Utils;

public class ImageCache {
	private static final String TAG="ImageCache";
	private static final int MAX_DURATION = 30 * 86400;   //30 days
	private static final String CACHE_DIR_NAME = ".GlobalCinemaImage";
	private static final String API_CACHE_PREFS = "ImageCachePreferences";

	private String mBaseCacheDir;
	private File mCacheDir;
	private HashMap<String, Long> mCacheInfo = new HashMap<String, Long>();
	private Context mContext;
	private SharedPreferences mPrefs;
	private static final Object instanceLock = new Object();

	private static ImageCache imageCache = null;

	public static ImageCache getInstance(Context context){
		if (imageCache != null) return imageCache;
		synchronized(instanceLock){
			imageCache = new ImageCache(context);
			return imageCache;
		}
	}
	
	public static void destroy(){
		imageCache = null;
	}

	private ImageCache(Context context){
		this.mContext = context;
		this.mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		//Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			this.mCacheDir=new File(android.os.Environment.getExternalStorageDirectory(), CACHE_DIR_NAME);
		else {
			this.mCacheDir=context.getCacheDir();
		}
		makeCacheJSONDir();
		this.mBaseCacheDir = this.mCacheDir.getAbsolutePath();
		loadCacheJSON();
	}

	private void makeCacheJSONDir() {
		try{
			if(mCacheDir!=null && !mCacheDir.exists()){
				mCacheDir.mkdirs();
			}
		}
		catch(Exception e){
			Log.e(TAG, e.getMessage(), e);
		}
	}

	private void loadCacheJSON(){
		synchronized(mCacheInfo){
			mCacheInfo = new HashMap<String, Long>();
		}
//		//Log.i(TAG, "loadCacheJSON");
		try{
			JSONObject cacheInfoAsJSON = new JSONObject(mPrefs.getString(API_CACHE_PREFS, "{}"));
			@SuppressWarnings("unchecked")
			Iterator<String> iter = cacheInfoAsJSON.keys();
			String key = "";
			while(iter.hasNext()){
				key = iter.next();
				synchronized(mCacheInfo){
					mCacheInfo.put(key, cacheInfoAsJSON.getLong(key));
				}
			}
		}
		catch (Exception e){
			Log.e(TAG, e.getMessage(), e);
			synchronized(mCacheInfo){
				mCacheInfo = new HashMap<String, Long>();
			}
		}
	}

	public void saveCacheJSON(){
		String key;
		JSONObject cacheInfoAsJSON = new JSONObject();
//		//Log.i(TAG, "saveCacheJSON");
		synchronized(mCacheInfo){
			Iterator<String> iter = mCacheInfo.keySet().iterator();
			try{
				while (iter.hasNext()){
					key = iter.next();
					cacheInfoAsJSON.put(key, mCacheInfo.get(key).longValue());
				}
			}
			catch(Exception e){
				Log.e(TAG, e.getMessage(), e);
				cacheInfoAsJSON = new JSONObject();
			}
		}
		Editor edit = mPrefs.edit();
		edit.putString(API_CACHE_PREFS, cacheInfoAsJSON.toString());
		edit.commit();
	}

	public Bitmap getImageFromCache(String key){
		Bitmap img = null;
		try{
			long currentTime = Utils.getCurrentTimeSecs();
			Long expireTime = null;
			//Log.i(TAG, "getAPIResultAsJSON");
			synchronized(mCacheInfo){
				expireTime = mCacheInfo.get(key);
			}
			if (expireTime != null && expireTime.longValue() > currentTime){
				img = BitmapFactory.decodeFile(getLocalPath(key));
			}
		}
		catch (Exception e){
			Log.e(TAG, e.getMessage(), e);
			img = null;
		}
		return img;
	}

	public Bitmap saveImageToCache(String key, String url, int maxDuration){
		Bitmap result = null;
		InputStream is=null;
		OutputStream os=null;
		int duration = maxDuration == 0 ? MAX_DURATION : maxDuration;
		try{
			//Log.i(TAG, "Saving file to " + getLocalPath(key));
			HttpURLConnection conn=null;
			File f = new File(getLocalPath(key));
			URL imageUrl = new URL(url);
			conn = (HttpURLConnection)imageUrl.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			is=conn.getInputStream();         
			os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			result = Utils.decodeFile(f);
			long currentTime = Utils.getCurrentTimeSecs();
			synchronized(mCacheInfo){
				mCacheInfo.put(key, currentTime + duration);
			}
		}
		catch (Exception e){
//			Log.e(TAG, e.getMessage(), e);
			result = null;
		}
		finally{
			try{
				if (os!=null) os.close();
				if (is!=null) is.close();
			}
			catch(Exception e){
				Log.e(TAG, e.getMessage(),e);
			}
		}
		return result;
	}

	public void cleanUpImageCache(){
		List<String> keysToRemove = new ArrayList<String>();
		File f;
		String key;
		long currentTime = Utils.getCurrentTimeSecs();
		synchronized(mCacheInfo){
			Iterator<String> iter = mCacheInfo.keySet().iterator();
			while(iter.hasNext()){
				try{
					key = iter.next();
					//Log.i(TAG, key);
					long expireTime = mCacheInfo.get(key).longValue();
					if (expireTime < currentTime){
						keysToRemove.add(key);
						iter.remove();
					}
				}
				catch (Exception e){
					Log.e(TAG, e.getMessage(), e);
					break;
				}
			}
		}
		//Log.i(TAG, "Number of cache to be cleaned " + keysToRemove.size()); 
		for(int i=0; i < keysToRemove.size(); i++){
			// Clean up
			key = keysToRemove.get(i);
			f = new File(getLocalPath(key));
			FileUtils.deleteQuietly(f);
		}
	}

	private String getLocalPath(String filename){
		return mBaseCacheDir+ "/"+ filename;
	}    
}
