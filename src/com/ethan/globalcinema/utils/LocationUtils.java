package com.ethan.globalcinema.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class LocationUtils {
	private static final String TAG = "LocationUtils";
	public static final String CURRENT_COUNTRY_PREF = "current_country";
	public static final String CURRENT_COUNTRY_NAME_PREF = "current_country_name";
	public static final String UNKNONW_LOCATION = "zz";
	
	private static Location location;
	
	public static Location getLocation(){
		return location;
	}

	public static String getCurrentCountry(Context context, boolean force){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		String location = pref.getString(CURRENT_COUNTRY_PREF, null);

//		if (location == null || force){
//			getCountryCode(context);
//		}	
		return location;
	}
	
	public static String getCurrentCountryName(Context context){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		String location = pref.getString(CURRENT_COUNTRY_NAME_PREF, null);
		return location;
	}

	public static void saveCurrentCountryCode(Context context, String currentCountry){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(CURRENT_COUNTRY_PREF, currentCountry);
		editor.commit();
	}
	
	public static void saveCurrentCountryName(Context context, String countryName){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(CURRENT_COUNTRY_NAME_PREF, countryName);
		editor.commit();
	}

	public static void getCountryCodeFromDevice(Context context){
		try{
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			String countryCode = tm.getSimCountryIso();
			LocationUtils.saveCurrentCountryCode(context, countryCode);
		}
		catch (Exception e){
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	public static Location getLastKnownLocation(Context context){
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
		return lastKnownLocation;
	}

	public static void getCountryCode(Context context){
		MyLocationListener locationListener = new MyLocationListener(context);
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
		if (lastKnownLocation != null) {
			location = lastKnownLocation;
			Geocoder geo = new Geocoder(context);
			try {
				Address addr = geo.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1).get(0);
				if (addr.getCountryCode() != null){
					LocationUtils.saveCurrentCountryCode(context, addr.getCountryCode());
				}
				if (addr.getCountryName() != null){
					LocationUtils.saveCurrentCountryName(context, addr.getCountryName());
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
				locationManager.requestLocationUpdates(locationProvider, 1, 0, locationListener);
			}
		}
		else{
			locationManager.requestLocationUpdates(locationProvider, 1, 0, locationListener);
		}
	}

	private static class MyLocationListener implements LocationListener {
		
		private Context context;
		
		public MyLocationListener(Context context){
			this.context = context;
		}
		@Override
		public void onLocationChanged(Location loc) {
			location = loc;
			if (loc != null) {
				Geocoder geo = new Geocoder(context);
				try {
					Address addr = geo.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1).get(0);
					Log.e(TAG, "Country code is by GPS is " + addr.getCountryCode());
					if (addr.getCountryCode() != null){
						LocationUtils.saveCurrentCountryCode(context, addr.getCountryCode());
					}
				} catch (Exception e) {
					Log.e(TAG, e.getMessage(), e);
					getCountryCodeFromDevice(context);
				}
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}
}

