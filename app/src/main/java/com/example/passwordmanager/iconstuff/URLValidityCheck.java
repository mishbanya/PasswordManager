package com.example.passwordmanager.iconstuff;
import java.net.MalformedURLException;
import java.net.URL;
public class URLValidityCheck {
    public static boolean isValidURL(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
