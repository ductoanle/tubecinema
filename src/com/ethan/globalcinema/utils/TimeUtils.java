package com.ethan.globalcinema.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;
import android.util.Log;

public class TimeUtils {
	
	private static final String TAG = "TimeUtils";

    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;
    public final static long DAYS = 7;
    
    public final static long ONE_WEEK = ONE_DAY * 7;
    
    public static long getMillisFromDHMS(String dhms){
    	long time = 0;
    	try{
    		SimpleDateFormat formatter ; 
        	Date date ; 
        	formatter = new SimpleDateFormat("MMMMM dd, yyyy HH:mm");
        	date = (Date)formatter.parse(dhms);
        	time = date.getTime();
    	}
    	catch(Exception e){
    		Log.e(TAG, e.getMessage(), e);
    	}
    	return time;
    }

    /**
     * converts time (in milliseconds) to human-readable format
     *  "<w> days, <x> hours, <y> minutes and (z) seconds"
     */
    public static String millisToLongDHMS(long duration) {
      StringBuffer res = new StringBuffer();
      long temp = 0;
	  if (duration >= ONE_SECOND) {
		temp = duration / ONE_WEEK;
		if (temp > 0) {
		  duration -= temp * ONE_WEEK;
		  res.append(temp).append(" week").append(temp > 1 ? "s" : "")
		  .append(duration >= ONE_MINUTE ? " " : "");
		}  
		if (res.length() > 0){
        	return res.toString();
        }
		
        temp = duration / ONE_DAY;
        if (temp > 0) {
          duration -= temp * ONE_DAY;
          res.append(temp).append(" day").append(temp > 1 ? "s" : "")
             .append(duration >= ONE_MINUTE ? " " : "");
        }
        if (res.length() > 0){
        	return res.toString();
        }

        temp = duration / ONE_HOUR;
        if (temp > 0) {
          duration -= temp * ONE_HOUR;
          res.append(temp).append(" hour").append(temp > 1 ? "s" : "")
             .append(duration >= ONE_MINUTE ? " " : "");
        }
        
        if (res.length() > 0){
        	return res.toString();
        }

        temp = duration / ONE_MINUTE;
        if (temp > 0) {
          duration -= temp * ONE_MINUTE;
          res.append(temp).append(" minute").append(temp > 1 ? "s" : "");
        }
        
        if (res.length() > 0){
        	return res.toString();
        }

        if (!TextUtils.isEmpty(res.toString()) && duration >= ONE_SECOND) {
          res.append(" and ");
        }

        temp = duration / ONE_SECOND;
        if (temp > 0) {
          res.append(temp).append(" second").append(temp > 1 ? "s" : "");
        }
        return res.toString();
      } else {
        return "1 minute";
      }
    }
}