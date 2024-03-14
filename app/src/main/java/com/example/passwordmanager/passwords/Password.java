package com.example.passwordmanager.passwords;

public class Password {
    private String password;
    private String host;
    private String iconURL = "";
    private String iconBase64 = "";

    Password() {
        this.password = "1234";
        this.host = "vk.com";
    }

    public Password(String password, String host) {
        this.password = password;
        this.host = host;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    public String getIconURL() {
        return this.iconURL;
    }
    public void setIconURL(String iconURL){this.iconURL  = iconURL;}
    public String getIconBase64(){
        return  this.iconBase64;
    }
    public void setIconBase64(String iconBase64){this.iconBase64  = iconBase64;}
}
