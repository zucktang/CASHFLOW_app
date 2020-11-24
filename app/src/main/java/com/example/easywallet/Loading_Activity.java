package com.example.easywallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

public class Loading_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() { // กำหนด delay เพื่อโหลดก่อนเข้าหน้า Main
                Intent intent = new Intent(Loading_Activity.this,MainActivity.class);
                startActivity(intent); // จากนั้นไปหน้า Main
                finish();
            }
        },1500);
    }
}
