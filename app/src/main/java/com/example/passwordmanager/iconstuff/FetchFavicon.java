package com.example.passwordmanager.iconstuff;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchFavicon extends AsyncTask<Void, Void, String> {
    private static final String TAG = "FetchIconTask";
    private static String host;
    private final OnIconFetchedListener listener;

    public FetchFavicon(String host, OnIconFetchedListener listener) {
        FetchFavicon.host = host;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String icon = "";
        OkHttpClient client = new OkHttpClient();
        String API_URL = "https://besticon-demo.herokuapp.com/allicons.json?url=" + host;
        if(!URLValidityCheck.isValidURL(API_URL)){
            Log.e(TAG,"URL is not valid");
            return icon;
        }
        Request request = new Request.Builder()
                .url(API_URL)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
                JsonArray itemsArray = jsonObject.getAsJsonArray("icons");
                if(!itemsArray.isEmpty()) {
                    icon = itemsArray.get(0).getAsJsonObject().get("url").getAsString();
                    Log.e(TAG, icon);
                }else{
                    Log.e(TAG, "No such icon");
                    icon="";
                }
            }else {
                Log.e(TAG, "Response is not successful");
                icon="";
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
            icon="";
        }
        return icon;
    }
    @Override
    protected void onPostExecute(String icon) {
        if (listener != null) {
            if (!icon.isEmpty()) {
                listener.onIconFetched(icon);
            } else {
                listener.onIconFetched("");
            }
        }
    }
    public interface OnIconFetchedListener{
        void onIconFetched(String icon);
    }
}