package com.example.passwordmanager.managers;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.passwordmanager.passwords.MasterPassword;

public class MasterPasswordDialog {

    private final Context context;
    private MasterPassword masterPassword;
    private final PrefManager prefManager;

    public MasterPasswordDialog(Context context, MasterPassword masterPassword, PrefManager prefManager) {
        this.context = context;
        this.masterPassword = masterPassword;
        this.prefManager = prefManager;
    }

    public interface MasterPasswordDialogListener {
        void onMasterPasswordEntered(boolean authenticated);
    }

    public void showMasterPasswordDialog(MasterPasswordDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Введите мастер-пароль");

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String enteredPassword = input.getText().toString();
            boolean authenticated = masterPassword.authenticate(enteredPassword);
            if (authenticated) {
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Неверный мастер-пароль", Toast.LENGTH_SHORT).show();
            }
            listener.onMasterPasswordEntered(authenticated);
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> {
            dialog.dismiss();
            listener.onMasterPasswordEntered(false);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showSetMasterPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Установите мастер-пароль");
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String enteredPassword = input.getText().toString();
                        if (enteredPassword.isEmpty()) {
                            input.setError("Пожалуйста, введите пароль");
                        } else {
                            masterPassword = new MasterPassword(enteredPassword);
                            prefManager.saveMasterPassword(masterPassword);
                            Toast.makeText(context, "Новый мастер-пароль установлен!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();
    }
}
