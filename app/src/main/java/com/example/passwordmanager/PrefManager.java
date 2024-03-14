package com.example.passwordmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrefManager {

    private static final String PASSWORDS_KEY = "passwords";
    private Context context;
    private Gson gson;

    public PrefManager(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public void addPassword(Password password) {
        List<Password> passwords = getPasswords();
        passwords.add(password);
        savePasswordDetails(passwords);
    }

    public Password findPassword(String host) {
        List<Password> passwords = getPasswords();
        for (Password password : passwords) {
            if (TextUtils.equals(password.getHost(), host)) {
                return password;
            }
        }
        return null;
    }

    public void removePassword(String host) {
        List<Password> passwords = getPasswords();
        for (Password password : passwords) {
            if (TextUtils.equals(password.getHost(), host)) {
                passwords.remove(password);
                savePasswordDetails(passwords);
                return;
            }
        }
    }
    public void removeAllPasswords() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PASSWORDS_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(PASSWORDS_KEY).apply();
    }
    public void savePasswordDetails(List<Password> passwords) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PASSWORDS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String passwordsJson = gson.toJson(passwords);
        editor.putString(PASSWORDS_KEY, passwordsJson);
        editor.apply();
    }

    public List<Password> getPasswords() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PASSWORDS_KEY, Context.MODE_PRIVATE);
        String passwordsJson = sharedPreferences.getString(PASSWORDS_KEY, "");

        if (!TextUtils.isEmpty(passwordsJson)) {
            Type listType = new TypeToken<ArrayList<Password>>() {}.getType();
            return gson.fromJson(passwordsJson, listType);
        } else {
            return new ArrayList<>();
        }
    }
}