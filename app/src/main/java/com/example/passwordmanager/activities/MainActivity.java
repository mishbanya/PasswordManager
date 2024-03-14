package com.example.passwordmanager.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passwordmanager.passwords.MasterPassword;
import com.example.passwordmanager.passwords.Password;
import com.example.passwordmanager.PrefManager;
import com.example.passwordmanager.R;
import com.example.passwordmanager.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private List<Password> data = new ArrayList<>();
    private MasterPassword masterPassword = null;
    RecyclerAdapter recyclerAdapter;
    PrefManager prefManager = new PrefManager(this);
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
        if (masterPassword != null) {
            showMasterPasswordDialog();
        } else {
            showSetMasterPasswordDialog();
        }

        data = prefManager.getPasswords();
        RecyclerView Recycler = findViewById(R.id.recycler);
        recyclerAdapter = new RecyclerAdapter(this, data);
        Recycler.setAdapter(recyclerAdapter);
        Recycler.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter.setOnEditButtonClickListener(password -> {

        });
        recyclerAdapter.setOnViewButtonClickListener(password -> {

        });
    }

    private void showMasterPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите мастер-пароль");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String enteredPassword = input.getText().toString();
            if (masterPassword.authenticate(enteredPassword)) {
                dialog.dismiss();
            } else {
                Toast.makeText(MainActivity.this, "Неверный мастер-пароль", Toast.LENGTH_SHORT).show();
                showMasterPasswordDialog();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showSetMasterPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Установите мастер-пароль");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String enteredPassword = input.getText().toString();
            masterPassword = new MasterPassword(enteredPassword);
            prefManager.saveMasterPassword(masterPassword);
            Toast.makeText(MainActivity.this, "Новый мастер-пароль установлен!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}