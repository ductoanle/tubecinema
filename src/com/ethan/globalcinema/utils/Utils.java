package com.ethan.globalcinema.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utils {
	private static final String TAG="Utils";
	private static final int HONEYCOMB_VERSION_CODE = 11;
	private static final int JELLYBEAN_VERSION_CODE = 16;

	public static boolean isSDKAbove(int version) {
		return android.os.Build.VERSION.SDK_INT >= version;
	}

	public static void CopyStream(InputStream is, OutputStream os)
	{
		final int buffer_size=1024;
		try
		{
			byte[] bytes=new byte[buffer_size];
			for(;;)
			{
				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch(Exception e){
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	public static boolean isTablet(Context context)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (metrics.heightPixels >= 728 && metrics.widthPixels >= 728);
    }
	
	public static boolean isAboveHoneyComb(){
		boolean isHoneycomb = android.os.Build.VERSION.SDK_INT >= HONEYCOMB_VERSION_CODE;
		return isHoneycomb;
	}
	
	public static boolean isJellyBean(){
		boolean isJellyBean = android.os.Build.VERSION.SDK_INT >= JELLYBEAN_VERSION_CODE;
		return isJellyBean;
	}

	//decodes image and scales it to reduce memory consumption
	public static Bitmap decodeFile(File f){
//		final String USING_SAMPLE_BITMAP = "usingSampleBitmap";
//		final String BITMAP_REQUIRED_SIZE = "bitmapRequiredSize";

//		if (VikiApplication.getVikiProperties().get(USING_SAMPLE_BITMAP).equals("false")){
			return decodeFileWithoutScaling(f);
//		}

//		try {
//			//decode image size
//			BitmapFactory.Options o = new BitmapFactory.Options();
//			// does not load the real bitmap just its metadata
//			o.inJustDecodeBounds = true;
//			BitmapFactory.decodeStream(new FileInputStream(f),null,o);
//
//			//Find the correct scale value. It should be the power of 2.
//			final int REQUIRED_SIZE=Integer.parseInt((String)VikiApplication.getVikiProperties().get(BITMAP_REQUIRED_SIZE));
//			int width_tmp=o.outWidth, height_tmp=o.outHeight;
//
//			int scale=1;
//			while(true){
//				if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
//					break;
//				width_tmp/=2;
//				height_tmp/=2;
//				scale*=2;
//			}
//
//			//decode with inSampleSize
//			BitmapFactory.Options o2 = new BitmapFactory.Options();
//			o2.inSampleSize=scale;
//			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
//		} catch (FileNotFoundException e) {
//			Log.e(TAG, e.getMessage(), e);
//		}
//		return null;
	}

	public static Bitmap decodeFileWithoutScaling(File f){
		try {
			FileInputStream fis = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			fis.close();
			return bitmap;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}
	}

	public static String escapeQuote(String s){
		return s.replace("'", "''");
	}

	public static String normalisedVersion(String version, String sep, int maxWidth) {
		String[] split = Pattern.compile(sep, Pattern.LITERAL).split(version);
		StringBuilder sb = new StringBuilder();
		for (String s : split) {
			sb.append(String.format("%" + maxWidth + 's', s));
		}
		return sb.toString();
	}

	public static long getCurrentTimeSecs(){
		return Calendar.getInstance().getTimeInMillis()/1000;
	}

	public static long getCurrentTimeMillis(){
		return Calendar.getInstance().getTimeInMillis();
	}

	public static HashMap<String, String> JSONToHashMap(String jsonString){
		HashMap<String, String> map = new HashMap<String,String>();
		try{
			JSONObject jsonObj = new JSONObject(jsonString);
			Iterator<String> iter = jsonObj.keys();
			String key;
			while (iter.hasNext()){
				key = iter.next();
				map.put(key, jsonObj.getString(key));
			}
		}
		catch (Exception e){
			Log.e(TAG, e.getMessage(), e);
			map = null;
		}

		return map;
	}

	public static JSONObject HashMapToString(HashMap<String, String> map){
		JSONObject jsonObj = new JSONObject();
		try{
			Iterator<String> iter = map.keySet().iterator();
			String key;
			while(iter.hasNext()){
				key = iter.next();
				jsonObj.put(key, map.get(key));
			}
		}
		catch (Exception e){
			Log.e(TAG, e.getMessage(), e);
			jsonObj = null;
		}
		return jsonObj;
	}

	public static String analyticsEvent(String...parts) {
		if (parts.length == 0) return null;

		StringBuilder sb = new StringBuilder();
		for (String part : parts) {
			if (!part.startsWith("/")) {
				sb.append('/');
			}

			if (part.endsWith("/")) {
				sb.append(part.substring(0, part.length() - 1));
			}
			else {
				sb.append(part);
			}
		}
		return sb.toString();
	}

	public static void clearBitmap(Drawable drawable){
		try{
			if (drawable != null && drawable instanceof BitmapDrawable) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
				Bitmap bitmap = bitmapDrawable.getBitmap();
				bitmap.recycle();
			}
		}
		catch (Exception e){
			Log.e(TAG, e.getMessage(),e);
		}
	}
	
	public static String clearForwardSlash(String str){
		return StringUtils.remove(str, "/");
	}
	
	public static double convertGramToOuz(double value) {
	    return value * 0.035274;
	}
	
	public static double convertOuzToGram(double value) {
	    return value / 0.035274;
	}
	
	public static double to2DecimalFormat(double value) {
		try{
			DecimalFormat df = new DecimalFormat("#.##");
	        return Double.parseDouble(df.format(value));
		}
		catch (NumberFormatException e){
			try{
				DecimalFormat df = new DecimalFormat("#,##");
				return Double.parseDouble(df.format(value));
			}
			catch (NumberFormatException ne){
				return 0;
			}
		}
	}
	
	public static boolean isListValid(List<? extends Object> list){
		return list != null && !list.isEmpty();
	}
}