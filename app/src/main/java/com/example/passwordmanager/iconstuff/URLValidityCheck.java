package com.example.passwordmanager.iconstuff;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
public class URLValidityCheck {
    private static final String TAG = "URLValidity";
    public static boolean isValidURL(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            Log.e(TAG,"MalformedURLException", e);
            return false;
        }
    }
}
