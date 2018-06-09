package com.restaurantbooking.mobiledevpc.daphnisysdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

    private Context mContext;
    public FingerprintHandler(Context context) {
        this.mContext = context;
    }

    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return;
        fingerprintManager.authenticate(cryptoObject,cancellationSignal,0,this,null);


    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Toast.makeText(mContext, "Authentication failec", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        Toast.makeText(mContext, "Authentication success", Toast.LENGTH_SHORT).show();
        mContext.startActivity(new Intent(mContext,MainActivity.class));
    }


}
