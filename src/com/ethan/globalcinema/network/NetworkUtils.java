package com.ethan.globalcinema.network;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;

import android.util.Log;

import com.foxykeep.datadroid.exception.RestClientException;
import com.foxykeep.datadroid.network.NetworkConnection;
import com.foxykeep.datadroid.network.NetworkConnection.NetworkConnectionResult;

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";
    
    public static int METHOD_GET = NetworkConnection.METHOD_GET;
    public static int METHOD_POST = NetworkConnection.METHOD_POST;
    public static int METHOD_PUT = NetworkConnection.METHOD_PUT;
    public static int METHOD_DELETE = NetworkConnection.METHOD_DELETE;
    
    public static String getResponse(String requestUrl, ErrorHandler handler, int method) {
        try {
            NetworkConnectionResult results = NetworkConnection.retrieveResponseFromService(requestUrl, method);
            return results.wsResponse;
        }
        catch (Exception e) {
            handleException(handler, e);
        }
        return null;
    }
    
    public static String getResponse(String requestUrl, ErrorHandler handler, int method, HashMap<String, String> parameters) {
        try {
            NetworkConnectionResult results = NetworkConnection.retrieveResponseFromService(requestUrl, method, parameters);
            return results.wsResponse;
        }
        catch (Exception e) {
            handleException(handler, e);
        }
        return null;
    }
    
    public static String getResponse(String requestUrl, ErrorHandler handler, int method, HashMap<String, String> parameters, ArrayList<Header> headers) {
        try {
            NetworkConnectionResult results = NetworkConnection.retrieveResponseFromService(requestUrl, method, parameters, headers);
            return results.wsResponse;
        }
        catch (Exception e) {
            handleException(handler, e);
        }
        return null;
    }
    
    public static String getResponse(String requestUrl, ErrorHandler handler, int method, HashMap<String, String> parameters, ArrayList<Header> headers, boolean isGzipEnabled) {
        try {
            NetworkConnectionResult results = NetworkConnection.retrieveResponseFromService(requestUrl, method, parameters, headers, isGzipEnabled);
            return results.wsResponse;
        }
        catch (Exception e) {
            handleException(handler, e);
        }
        return null;
    }
    
    public static String getResponse(String requestUrl, ErrorHandler handler, int method, HashMap<String, String> parameters, ArrayList<Header> headers, boolean isGzipEnabled, String userAgent) {
        try {
            NetworkConnectionResult results = NetworkConnection.retrieveResponseFromService(requestUrl, method, parameters, headers, isGzipEnabled, userAgent);
            return results.wsResponse;
        }
        catch (Exception e) {
            handleException(handler, e);
        }
        return null;
    }
    
    private static void handleException(ErrorHandler handler, Exception e) {
    	if (handler != null){
    		if (e instanceof RestClientException) {
                handler.handleRestClientException((RestClientException)e);
            }
            else {
                handler.handleOtherException(e);
            }
    	}
    	else{
    		Log.e(TAG, e.getMessage(), e);
    	}
    }
}
