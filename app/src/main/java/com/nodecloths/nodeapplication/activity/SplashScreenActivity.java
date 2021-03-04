package com.nodecloths.nodeapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.helper.Common;

public class SplashScreenActivity extends AppCompatActivity {
    private int progress;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String shp_Phone, shp_Code,first;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressBar);
        doWork();
        /** SharedPreferences */
        sharedPreferences = getSharedPreferences(Common.MYPRA_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        shp_Phone = sharedPreferences.getString(Common.PHONE_NUMBER, "");
        shp_Code = sharedPreferences.getString(Common.CODE_NUMBER, "");
        first = sharedPreferences.getString(Common.FIRST, "");

        if (first.isEmpty()){
            editor.putString(Common.FABRICS, "FAbrics");
            editor.putString(Common.STOCKLOT, "STockLot");
            editor.putString(Common.FACTORY, "FActory");
            editor.putString(Common.QUOTE, "QUote");
            editor.putString(Common.FIRST, "ontEmpty");
            editor.putString(Common.ON_OFF, "ON");
            editor.commit();
        }

        Common.selectFragment = "fabrics";

    }

    public void doWork() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 10; i < 110; i = i + 10) {
                    try {
                        Thread.sleep(400);
                        progressBar.setProgress(progress);
                        progress = progress + 10;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (progress == 100) {
                    if (shp_Phone.isEmpty() && shp_Code.isEmpty()) {
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }
        });
        thread.start();

    }
}
