package com.example.passwordmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.passwordmanager.managers.MasterPasswordDialog;
import com.example.passwordmanager.passwords.MasterPassword;
import com.example.passwordmanager.passwords.Password;
import com.example.passwordmanager.managers.PrefManager;
import com.example.passwordmanager.R;
import com.example.passwordmanager.managers.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Password> data = new ArrayList<>();
    private MasterPassword masterPassword = null;
    RecyclerAdapter recyclerAdapter;
    PrefManager prefManager = new PrefManager(this);
    MasterPasswordDialog masterPasswordDialog;
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAdd = findViewById(R.id.buttonAddPassword);
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PasswordAddActivity.class);
            startActivity(intent);
        });

        masterPassword = prefManager.getMasterPassword();
        masterPasswordDialog = new MasterPasswordDialog(this,masterPassword,prefManager);
        if (masterPassword == null) {
            masterPasswordDialog.showSetMasterPasswordDialog();
        }

        data = prefManager.getPasswords();
        RecyclerView Recycler = findViewById(R.id.recycler);
        recyclerAdapter = new RecyclerAdapter(this, data);
        Recycler.setAdapter(recyclerAdapter);
        Recycler.setLayoutManager(new LinearLayoutManager(this));

        recyclerAdapter.setOnViewButtonClickListener(password -> {
            masterPasswordDialog.showMasterPasswordDialog(authenticated -> {
                if (authenticated) {
                    int position = data.indexOf(password);
                    if (position != RecyclerView.NO_POSITION) {
                        RecyclerAdapter.ViewHolder viewHolder = (RecyclerAdapter.ViewHolder) Recycler.findViewHolderForAdapterPosition(position);
                        if (viewHolder != null) {
                            viewHolder.textViewPassword.setText(password.getPassword());
                        }
                    }
                }
            });
        });
        recyclerAdapter.setOnEditButtonClickListener(password -> {
            masterPasswordDialog.showMasterPasswordDialog(authenticated -> {
                if (authenticated) {
                    int position = data.indexOf(password);
                    if (position != RecyclerView.NO_POSITION) {

                    }
                }
            });
        });
    }
}