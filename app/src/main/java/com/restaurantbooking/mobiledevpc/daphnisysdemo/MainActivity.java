package com.restaurantbooking.mobiledevpc.daphnisysdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.buttonEnglish)).setOnClickListener(v -> {
            startMenuActivity("English");
        });
        (findViewById(R.id.buttonArab)).setOnClickListener(v -> {
            startMenuActivity("Arab");
        });


    }

    private void startMenuActivity(String type) {
        startActivity(new Intent(this,MenuActivity.class).putExtra("Type",type));
    }
}
