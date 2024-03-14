package com.example.passwordmanager.passwords;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MasterPassword {
    private String hash = null;

    public MasterPassword(){
    }
    public MasterPassword(String password){
        this.hash = hashPassword(password);
    }
    public String getHash(){return this.hash;}
    public void setHash(String hash){this.hash = hash;}

    public boolean authenticate(String enteredPassword) {
        if (hash != null) {
            String enteredHash = hashPassword(enteredPassword);
            return hash.equals(enteredHash);
        }
        return false;
    }

    public String hashPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
