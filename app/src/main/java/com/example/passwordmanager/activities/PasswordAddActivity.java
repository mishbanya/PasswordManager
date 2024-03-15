package com.example.passwordmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordmanager.passwords.Password;
import com.example.passwordmanager.managers.PrefManager;
import com.example.passwordmanager.R;
import com.example.passwordmanager.iconstuff.FetchFavicon;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;

public class PasswordAddActivity extends AppCompatActivity {

    Button buttonAdd;
    Button buttonCancel;
    EditText editTextHost;
    TextView textViewLoading;
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
        textViewLoading = findViewById(R.id.textViewLoading);

        buttonAdd.setOnClickListener(v -> {
            String password = editTextPassword.getText().toString();
            String host = editTextHost.getText().toString();
            if (password.isEmpty() || host.isEmpty()) {
                Toast.makeText(PasswordAddActivity.this, "Оба поля должны быть заполнены", Toast.LENGTH_SHORT).show();
                return;
            }
            textViewLoading.setAlpha(1);
            try {
                FetchFavicon fetchFavicon = new FetchFavicon(host, icon -> {
                    if (icon.isEmpty()) {
                        prefManager.addPassword(new Password(password, host));
                        Log.e(TAG, "Icon WILL BE EMPTY");
                        Intent intent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                            intent = new Intent(PasswordAddActivity.this, MainActivity.class);
                        }
                        startActivity(intent);
                        return;
                    }
                    Password newPassword = new Password(password, host);
                    Picasso.get().load(icon).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                            newPassword.setIconBase64(base64Image);
                            prefManager.addPassword(newPassword);
                            StartMainActivity();
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            Log.e(TAG, "Bitmap load failed, ICON WILL BE EMPTY");
                            prefManager.addPassword(newPassword);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
                });
                fetchFavicon.execute();
            }catch (Exception e){
                prefManager.addPassword(new Password(password, host));
                Log.e(TAG,"Error while fetching, loading or converting favicon",e);
            }
        });
        buttonCancel.setOnClickListener(v -> StartMainActivity());
    }
    //Дабы в коде было меньше повторений
    public void StartMainActivity(){
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            intent = new Intent(PasswordAddActivity.this, MainActivity.class);
        }
        startActivity(intent);
    }
}
