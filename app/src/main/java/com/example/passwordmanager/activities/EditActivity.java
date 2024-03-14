package com.example.passwordmanager.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.example.passwordmanager.managers.PrefManager;
import com.example.passwordmanager.passwords.MasterPassword;
import com.example.passwordmanager.passwords.Password;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textViewHost;
    TextView textViewPassword;
    Button buttonEdit;
    Button buttonDelete;
    Button buttonCancel;
    Password password;
    private String TAG = "EditActivity";
    PrefManager prefManager = new PrefManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("password")) {
            password = (Password) intent.getSerializableExtra("password");
        }
        if(password==null){
            Log.e(TAG, "Password is null");
            Intent Errintent = new Intent(EditActivity.this, MainActivity.class);
            startActivity(Errintent);
        }
        textViewHost = findViewById(R.id.textViewHost);
        textViewPassword = findViewById(R.id.textViewPassword);
        textViewHost.setText(password.getHost());
        textViewPassword.setText(password.getPassword());

        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonEdit.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonEdit){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Установите новый пароль");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                String enteredPassword = input.getText().toString();
                Password newPassword = new Password(enteredPassword,password.getHost());
                newPassword.setIconURL(password.getIconURL());
                newPassword.setIconBase64(password.getIconBase64());
                prefManager.removePassword(password.getHost());
                prefManager.addPassword(newPassword);
                Toast.makeText(this, "Новый пароль установлен!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            });

            builder.setNegativeButton("Отмена", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();

        } else if (v == buttonDelete) {
            prefManager.removePassword(password.getHost());
            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (v == buttonCancel) {
            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}