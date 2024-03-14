package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.util.Base64;

import com.example.passwordmanager.iconstuff.FetchFavicon;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;

public class PasswordAddActivity extends AppCompatActivity{

    Button buttonAdd;
    Button buttonCancel;
    EditText editTextHost;
    String TAG = "PasswordAddActivity";
    EditText editTextPassword;
    PrefManager prefManager = new PrefManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_add);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonCancel = findViewById(R.id.buttonCancel);
        editTextHost = findViewById(R.id.editTextHost);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonAdd.setOnClickListener(v -> {
            String password = editTextPassword.getText().toString();
            String host = editTextHost.getText().toString();
            FetchFavicon fetchFavicon = new FetchFavicon(host, icon -> {
                if (!icon.isEmpty()) {
                    Password newPassword = new Password(password, host);
                    Picasso.get().load(icon).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                            newPassword.setIconURL(icon);
                            newPassword.setIconBase64(base64Image);
                            prefManager.addPassword(newPassword);
                            Intent intent = new Intent(PasswordAddActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            Log.e("Bitmap","Загрузка Bitmap завершена с ошибкой");
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                } else {
                    prefManager.addPassword(new Password(password,host));
                    Log.e(TAG, "Icon WILL BE EMPTY");
                    Intent intent = new Intent(PasswordAddActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            fetchFavicon.execute();
        });
        buttonCancel.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordAddActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
