package com.ethan.globalcinema.cache;

import android.app.IntentService;
import android.content.Intent;

public class CacheCleaningService extends IntentService {
    private static final String SERVICE_NAME="CacheCleaningService";

    @SuppressWarnings("unused")
    private static final String TAG="Service";

    public CacheCleaningService() {
        super(SERVICE_NAME);
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        APICache.getInstance(this).cleanUpAPICache();
        ImageCache.getInstance(this).cleanUpImageCache();
        APICache.destroy();
        ImageCache.destroy();
    }
}
