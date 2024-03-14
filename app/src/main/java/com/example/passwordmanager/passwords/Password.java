package com.example.passwordmanager.passwords;

import java.io.Serializable;
import java.util.Arrays;

public class Password implements Serializable {
    private String hash;
    private final String host;
    private String iconURL = "";
    private String iconBase64 = "";
    private final int key = 3;

    public Password(String password, String host) {
        this.hash = hashPassword(password);
        this.host = host;
    }

    public void setPassword(String password) {
        this.hash = hashPassword(password);
    }

    public String getPassword() {
        return dehashPassword(this.hash);
    }

    public String getHost() {
        return this.host;
    }

    public void setIconURL(String iconURL){this.iconURL  = iconURL;}
    public String getIconBase64(){
        return  this.iconBase64;
    }
    public void setIconBase64(String iconBase64){this.iconBase64  = iconBase64;}

    public String hashPassword(String password){
        char[] arr = password.toCharArray();
        for(int i = 0; i < arr.length; i++){
            arr[i] = (char) ((arr[i] + key) % 128);
        }
        return String.valueOf(arr);
    }

    public String dehashPassword(String hash){
        char[] arr = hash.toCharArray();
        for(int i = 0; i < arr.length; i++){
            arr[i] = (char) ((arr[i] + 128 - key % 128) % 128);
        }
        return String.valueOf(arr);
    }
}
