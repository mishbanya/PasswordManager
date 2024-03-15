package com.example.passwordmanager.managers;

import android.content.Context;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.RequiresApi;

import java.util.concurrent.Executor;

@RequiresApi(api = Build.VERSION_CODES.P)
public class FingerprintAuthentication {

    private final Executor executor;
    private final BiometricPrompt biometricPrompt;
    private final BiometricPrompt.AuthenticationCallback authenticationCallback;
    private FingerprintAuthenticationCallback callback;

    public FingerprintAuthentication(Context context) {
        this.executor = context.getMainExecutor();
        this.authenticationCallback = createAuthenticationCallback();
        this.biometricPrompt = new BiometricPrompt.Builder(context)
                .setTitle("Аутентификация по отпечатку пальца")
                .setNegativeButton("Использовать мастер-пароль", executor, (dialog, which) -> callback.onAuthenticationCanceled())
                .build();
    }

    public void authenticate(FingerprintAuthenticationCallback callback) {
        this.callback = callback;
        CancellationSignal cancellationSignal = new CancellationSignal();
        biometricPrompt.authenticate(cancellationSignal, executor, authenticationCallback);
    }

    private BiometricPrompt.AuthenticationCallback createAuthenticationCallback() {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                callback.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationFailed() {
                //Попробуем еще раз
                super.onAuthenticationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (callback != null) {
                    callback.onAuthenticationSuccess();
                }
            }
        };
    }

    public interface FingerprintAuthenticationCallback {
        void onAuthenticationSuccess();
        void onAuthenticationError(int errorCode, CharSequence errString);
        void onAuthenticationCanceled();
    }
}