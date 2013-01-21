package com.ethan.globalcinema.utils;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import android.os.Bundle;

public class KeyGenerationUtils {
	private static final String TAG = "KeyGenerationUtils";
	private static final String prefix = "viki.com";
	
	public static String generateKey(String apiTag, Bundle params){
		String params_str = "";
		String key;
		Iterator<String> iter = params.keySet().iterator();
		while(iter.hasNext()){
			key = iter.next();
			params_str += "_" + key + "_" + escape(params.getString(key));
		}
		return apiTag  + params_str; 
	}
	
	public static String generateKey(String uri){
		return escape(uri);
	}
	
	public static String generateImageKey(String uri){
		return escape(escapeImagePrefix(uri));
	}
	
	private static String escapeImagePrefix(String str){
		if (str != null){
			int location = str.indexOf(prefix);
			if (location > 0){
				str = str.substring(location);
			}
		}
		return str;
	}
	
	private static String escape(String str){
		String[] ESCAPE_CHAR = {"/", ":", "."};
		for (int i = 0; i < ESCAPE_CHAR.length; i++){
			str = StringUtils.replace(str, ESCAPE_CHAR[i],"_");
		}
		return str;
	}
}
