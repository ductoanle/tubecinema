package com.ethan.globalcinema.utils;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.ethan.globalcinema.fragments.AlertDialogFragment;
import com.ethan.globalcinema.fragments.ProgressDialogFragment;

public class DialogUtils {
    private static final String TAG = "ProgressDialogUtils";
    
    public static void showAlertDialog(FragmentActivity activity, String tagName, String title, String message){
        try {
            DialogFragment newFragment = AlertDialogFragment.newInstance(title, message);
            newFragment.show(activity.getSupportFragmentManager(), tagName);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
    
    public static void showProgressDialog(FragmentActivity activity, String tagName){
        try{
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ProgressDialogFragment dialog = (ProgressDialogFragment)activity.getSupportFragmentManager().findFragmentByTag(tagName);
            if (dialog == null) {
                dialog = new ProgressDialogFragment();
                dialog.show(ft, tagName);
            }
        }
        catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
    }
    
    public static void dismissDialogFragment(FragmentActivity activity, String tagName){
        if (activity != null) {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (fm.findFragmentByTag(tagName) != null){
                ProgressDialogFragment f = (ProgressDialogFragment)fm.findFragmentByTag(tagName);
                f.dismiss();
                ft.remove(f);
            }
        }
    }
}
