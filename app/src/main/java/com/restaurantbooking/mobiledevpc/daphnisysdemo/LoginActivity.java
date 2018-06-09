package com.restaurantbooking.mobiledevpc.daphnisysdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class LoginActivity extends AppCompatActivity {

    private KeyStore keyStore;
    private static final String KEY_NAME = "Daphnisys";
    private Cipher cipher;
    private TextView textViewHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewHeading = findViewById(R.id.textViewHeading);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT},
                        1);
            } else
                authFingerprint();

        }
    }
    private void authFingerprint() {

        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager =(FingerprintManager)getSystemService(FINGERPRINT_SERVICE);

        if(!fingerprintManager.isHardwareDetected())
            Toast.makeText(this, "Finger authentication permission not enable", Toast.LENGTH_SHORT).show();
        else
        {
            if(!fingerprintManager.hasEnrolledFingerprints())
                Toast.makeText(this, "Register atleast one fingerprint in Settings", Toast.LENGTH_SHORT).show();
            else{
                if(!keyguardManager.isKeyguardSecure())
                    Toast.makeText(this, "Screen lock security not enable in Settings", Toast.LENGTH_SHORT).show();
                else {

                    textViewHeading.setText("Place finger on scanner");
                    genKey();

                    if (cipherInit()) {
                        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        FingerprintHandler helper = new FingerprintHandler(this);
                        helper.startAuthentication(fingerprintManager, cryptoObject);


                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int request_code, @NonNull String[] permissions, @NonNull int[] grant_results) {
        switch (request_code) {
            case 1:
                if (grant_results.length > 0 &&
                        grant_results[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                    authFingerprint();
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean cipherInit() {

        try {
          cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
            try {
                keyStore.load(null);
                SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return true;
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
                return false;
            } catch (CertificateException e1) {
                e1.printStackTrace();
                return false;
            } catch (UnrecoverableKeyException e1) {
                e1.printStackTrace();
                return false;
            } catch (KeyStoreException e1) {
                e1.printStackTrace();
                return false;
            } catch (InvalidKeyException e1) {
                e1.printStackTrace();
                return false;
            }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void genKey() {
        try {
           keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        KeyGenerator  keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build()
            );
            keyGenerator.generateKey();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }catch (InvalidAlgorithmParameterException e){
            e.printStackTrace();
        }

    }
}
