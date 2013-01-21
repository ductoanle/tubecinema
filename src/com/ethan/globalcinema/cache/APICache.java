package com.ethan.globalcinema.cache;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ethan.globalcinema.utils.Utils;

public class APICache {
    private static final String TAG=".APICache";
    private static final int MAX_DURATION = 30 * 60;   //1 day
    private static final String CACHE_DIR_NAME = ".GlobalCinemaApiCache";
    private static final String API_CACHE_PREFS = "ApiCachePreferences";

    private String mBaseCacheDir;
    private File mCacheDir;
    private HashMap<String, Long> mCacheInfo = new HashMap<String, Long>();
    private SharedPreferences mPrefs;
    private static final Object instanceLock = new Object();

    private static APICache apiCache = null;

    public static APICache getInstance(Context context){
        synchronized(instanceLock){
            if (apiCache != null) return apiCache;
            apiCache = new APICache(context);
            return apiCache;
        }
    }

    public static void destroy(){
        apiCache = null;
    }

    private APICache(Context context){
        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
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
        //      Log.i(TAG, "loadCacheJSON");
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
        //      Log.i(TAG, "saveCacheJSON");
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
    
    public String getAPIResultAsString(String key){
        String fileContent = null;
        try{
            long currentTime = Utils.getCurrentTimeSecs();
            Long expireTime = null;
            //          Log.i(TAG, "getAPIResultAsJSON");
            synchronized(mCacheInfo){
                expireTime = mCacheInfo.get(key);
            }
            if (expireTime != null && expireTime.longValue() > currentTime){
                File f = new File(getLocalPath(key));
                fileContent = FileUtils.readFileToString(f);
            }
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
            fileContent = null;
        }
        return fileContent;
    }

    public JSONObject getAPIResultAsJSON(String key){
        JSONObject obj = null;
        String fileContent = null;
        try{
            long currentTime = Utils.getCurrentTimeSecs();
            Long expireTime = null;
            synchronized(mCacheInfo){
                expireTime = mCacheInfo.get(key);
            }
            if (expireTime != null && expireTime.longValue() > currentTime){
                File f = new File(getLocalPath(key));
                fileContent = FileUtils.readFileToString(f);
                obj = new JSONObject(fileContent);
            }
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
            obj = null;
        }
        fileContent = null;
        return obj;
    }

    public void saveAPIResultAsJSON(String key, String content){
        try{
            File f = new File(getLocalPath(key));
            FileWriter writer = new FileWriter(f);
            writer.write(content);
            writer.flush();
            writer.close();
            long currentTime = Utils.getCurrentTimeSecs();
            synchronized(mCacheInfo){
                mCacheInfo.put(key, currentTime + MAX_DURATION);
            }
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void cleanupAPICacheBySignature(String sig){
        try{
            List<String> keysToRemove = new ArrayList<String>();
            File f;
            String key;
            synchronized(mCacheInfo){
                Iterator<String> iter = mCacheInfo.keySet().iterator();
                while(iter.hasNext()){
                    try{
                        key = iter.next();
                        if (StringUtils.contains(key, sig)){
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
            for(int i=0; i < keysToRemove.size(); i++){
                key = keysToRemove.get(i);
                f = new File(getLocalPath(key));
                FileUtils.deleteQuietly(f);
            }
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void cleanUpAPICache(){
        List<String> keysToRemove = new ArrayList<String>();
        File f;
        String key;
        long currentTime = Utils.getCurrentTimeSecs();
        synchronized(mCacheInfo){
            Iterator<String> iter = mCacheInfo.keySet().iterator();
            while(iter.hasNext()){
                try{
                    key = iter.next();
                    //                  Log.i(TAG, key);
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
        //      Log.i(TAG, "Number of cache to be cleaned " + keysToRemove.size()); 
        for(int i=0; i < keysToRemove.size(); i++){
            // Clean up
            key = keysToRemove.get(i);
            f = new File(getLocalPath(key));
            FileUtils.deleteQuietly(f);
        }
    }

    public void saveToCache(String content, String filename){
        try{
            File f = new File(getLocalPath(filename));
            FileWriter writer = new FileWriter(f);
            writer.write(content);
            writer.flush();
            writer.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public String readFromCache(String filename){
        final String EMPTY_JSON = "[]";
        try{
            File f = new File(getLocalPath(filename));
            return FileUtils.readFileToString(f);
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
            return EMPTY_JSON;
        }

    }

    private String getLocalPath(String filename){
        return mBaseCacheDir+ "/"+ filename;
    }    
}

