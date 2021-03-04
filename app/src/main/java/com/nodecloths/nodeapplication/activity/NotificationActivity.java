package com.nodecloths.nodeapplication.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.core.Tag;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.helper.Common;

import java.util.PriorityQueue;

public class NotificationActivity extends AppCompatActivity {

    private LinearLayout layout;
    private LinearLayout layoutFabrics;
    private LinearLayout layoutStockLot;
    private LinearLayout layoutFactory;
    private LinearLayout layoutQuote;
    private ImageView ivFabrics;
    private ImageView ivStockLot;
    private ImageView ivFactory;
    private ImageView ivQuote;
    private String st="ON";
    private Switch sw;
    private String fabrics, stock, factory, quote;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        /** SharedPreferences */
        sharedPreferences = getSharedPreferences(Common.MYPRA_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        st = sharedPreferences.getString(Common.ON_OFF, "");
        fabrics = sharedPreferences.getString(Common.FABRICS, "");
        stock = sharedPreferences.getString(Common.STOCKLOT, "");
        factory = sharedPreferences.getString(Common.FACTORY, "");
        quote = sharedPreferences.getString(Common.QUOTE, "");
        initComponents();
        isChecked();
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    st = sw.getTextOn().toString().trim();
                    layout.setVisibility(View.VISIBLE);
                } else {
                    st = sw.getTextOff().toString().trim();
                    layout.setVisibility(View.GONE);
                }
                editor.putString(Common.ON_OFF, st);
                editor.commit();
                //Toast.makeText(NotificationActivity.this, st, Toast.LENGTH_SHORT).show();
            }
        });


        layoutFabrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharedPreferences.getString(Common.FABRICS, "").isEmpty()) {
                    editor.putString(Common.FABRICS, "");
                    editor.commit();
                    ivFabrics.setImageResource(R.drawable.ic_check_circle);
                } else {
                    editor.putString(Common.FABRICS, "FAbrics");
                    editor.commit();
                    ivFabrics.setImageResource(R.drawable.ic_check_circle_ok);
                }
            }
        });
        layoutStockLot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharedPreferences.getString(Common.STOCKLOT, "").isEmpty()) {
                    editor.putString(Common.STOCKLOT, "");
                    editor.commit();
                    ivStockLot.setImageResource(R.drawable.ic_check_circle);
                } else {
                    editor.putString(Common.STOCKLOT, "STockLot");
                    editor.commit();
                    ivStockLot.setImageResource(R.drawable.ic_check_circle_ok);
                }
            }
        });
        layoutFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharedPreferences.getString(Common.FACTORY, "").isEmpty()) {
                    editor.putString(Common.FACTORY, "");
                    editor.commit();
                    ivFactory.setImageResource(R.drawable.ic_check_circle);
                } else {
                    editor.putString(Common.FACTORY, "FActory");
                    editor.commit();
                    ivFactory.setImageResource(R.drawable.ic_check_circle_ok);
                }
            }
        });
        layoutQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharedPreferences.getString(Common.QUOTE, "").isEmpty()) {
                    editor.putString(Common.QUOTE, "");
                    editor.commit();
                    ivQuote.setImageResource(R.drawable.ic_check_circle);
                } else {
                    editor.putString(Common.QUOTE, "QUote");
                    editor.commit();
                    ivQuote.setImageResource(R.drawable.ic_check_circle_ok);
                }
            }
        });

    }

    private void initComponents() {
        sw = findViewById(R.id.switch1);
        layout = findViewById(R.id.layout);
        layoutFabrics = findViewById(R.id.layoutFabrics);
        layoutStockLot = findViewById(R.id.layoutStockLot);
        layoutFactory = findViewById(R.id.layoutFactory);
        layoutQuote = findViewById(R.id.layoutQuote);
        ivFabrics = findViewById(R.id.ivFabrics);
        ivStockLot = findViewById(R.id.ivStockLot);
        ivFactory = findViewById(R.id.ivFactory);
        ivQuote = findViewById(R.id.ivQuote);
    }

    private void isChecked(){

        if (st.equals("ON")) {
            sw.setChecked(true);
            layout.setVisibility(View.VISIBLE);
        } else {
            sw.setChecked(false);
            layout.setVisibility(View.GONE);
        }

        if (!fabrics.isEmpty()) {
            ivFabrics.setImageResource(R.drawable.ic_check_circle_ok);
        } else {
            ivFabrics.setImageResource(R.drawable.ic_check_circle);
        }
        if (!stock.isEmpty()) {
            ivStockLot.setImageResource(R.drawable.ic_check_circle_ok);
        } else {
            ivStockLot.setImageResource(R.drawable.ic_check_circle);
        }
        if (!factory.isEmpty()) {
            ivFactory.setImageResource(R.drawable.ic_check_circle_ok);
        } else {
            ivFactory.setImageResource(R.drawable.ic_check_circle);
        }
        if (!quote.isEmpty()) {
            ivQuote.setImageResource(R.drawable.ic_check_circle_ok);
        } else {
            ivQuote.setImageResource(R.drawable.ic_check_circle);
        }
    }
}

