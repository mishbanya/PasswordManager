package com.example.passwordmanager.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.passwordmanager.managers.FingerprintAuthentication;
import com.example.passwordmanager.managers.MasterPasswordDialog;
import com.example.passwordmanager.passwords.MasterPassword;
import com.example.passwordmanager.passwords.Password;
import com.example.passwordmanager.managers.PrefManager;
import com.example.passwordmanager.R;
import com.example.passwordmanager.managers.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.P)
public class MainActivity extends AppCompatActivity {

    private List<Password> data = new ArrayList<>();
    RecyclerAdapter recyclerAdapter;
    PrefManager prefManager = new PrefManager(this);
    MasterPasswordDialog masterPasswordDialog;
    Button buttonAdd;
    String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAdd = findViewById(R.id.buttonAddPassword);
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PasswordAddActivity.class);
            startActivity(intent);
        });

        MasterPassword masterPassword = prefManager.getMasterPassword();
        masterPasswordDialog = new MasterPasswordDialog(this, masterPassword, prefManager);
        if (masterPassword == null) {
            masterPasswordDialog.showSetMasterPasswordDialog();
        }

        data = prefManager.getPasswords();
        RecyclerView recycler = findViewById(R.id.recycler);
        recyclerAdapter = new RecyclerAdapter(this, data);
        recycler.setAdapter(recyclerAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        FingerprintAuthentication fingerprintAuthentication = new FingerprintAuthentication(this);
        recyclerAdapter.setOnViewButtonClickListener(password -> fingerprintAuthentication.authenticate(new FingerprintAuthentication.FingerprintAuthenticationCallback() {
                                                   void showPassword(Password Password, RecyclerView Recycler) {
                                                       int position = data.indexOf(Password);
                                                       if (position != RecyclerView.NO_POSITION) {
                                                           RecyclerAdapter.ViewHolder viewHolder = (RecyclerAdapter.ViewHolder) Recycler.findViewHolderForAdapterPosition(position);
                                                           if (viewHolder != null) {
                                                               viewHolder.textViewPassword.setText(password.getPassword());
                                                           }
                                                       }
                                                   }

                                                   @Override
                                                   public void onAuthenticationSuccess() {
                                                       showPassword(password, recycler);
                                                   }

                                                   @Override
                                                   public void onAuthenticationError(int errorCode, CharSequence errString) {
                                                       Log.e(TAG, String.valueOf(errorCode) + errString);
                                                       Toast.makeText(MainActivity.this, "Ошибка аутентификации", Toast.LENGTH_SHORT).show();
                                                   }

                                                   @Override
                                                   public void onAuthenticationCanceled() {
                                                       masterPasswordDialog.showMasterPasswordDialog(authenticated -> {
                                                           if (authenticated) {
                                                               showPassword(password, recycler);
                                                           }
                                                       });
                                                   }
                                               }
        ));
        recyclerAdapter.setOnEditButtonClickListener(password -> fingerprintAuthentication.authenticate(new FingerprintAuthentication.FingerprintAuthenticationCallback() {
                                                   void startEditActivity(Password Password) {
                                                       int position = data.indexOf(Password);
                                                       if (position != RecyclerView.NO_POSITION) {
                                                           Intent intent = new Intent(MainActivity.this, EditActivity.class);
                                                           intent.putExtra("password", Password);
                                                           startActivity(intent);
                                                       }
                                                   }

                                                   @Override
                                                   public void onAuthenticationSuccess() {
                                                       startEditActivity(password);
                                                   }

                                                   @Override
                                                   public void onAuthenticationError(int errorCode, CharSequence errString) {
                                                       Log.e(TAG, String.valueOf(errorCode) + errString);
                                                       Toast.makeText(MainActivity.this, "Ошибка аутентификации", Toast.LENGTH_SHORT).show();
                                                   }

                                                   @Override
                                                   public void onAuthenticationCanceled() {
                                                       masterPasswordDialog.showMasterPasswordDialog(authenticated -> {
                                                           if (authenticated) {
                                                               startEditActivity(password);
                                                           }
                                                       });
                                                   }
                                               }
        ));
    }
}