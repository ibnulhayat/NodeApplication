package com.nodecloths.nodeapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private TextView tvSignUp;
    private EditText etLoginPhone, etLoginPassword;
    private Button loginButton;
    private ProgressBar progressBar;
    private String phone, pin_number;
    private RequestQueue mRequestQueue;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponent();


        /** Volley */
        mRequestQueue = Volley.newRequestQueue(this);
        /** SharedPreferences */
        sharedPreferences = getSharedPreferences(Common.MYPRA_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();


        /** Login button click */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = etLoginPhone.getText().toString().trim();
                pin_number = etLoginPassword.getText().toString().trim();
                login();
                progressBar.setVisibility(View.VISIBLE);
                hideKeyboard(v);
            }
        });

        /** SingUp button click */
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });
    }

    private void initComponent() {
        tvSignUp = findViewById(R.id.tvSignUp);
        etLoginPhone = findViewById(R.id.etLoginPhone);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
    }


    // seller api call below
    private void login() {
        mRequestQueue.getCache().clear();


        StringRequest request = new StringRequest(Request.Method.GET, Apis.login+phone,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object2 = new JSONObject(response);
                    JSONArray jsonArray = object2.getJSONArray("login");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String phn = object.getString("mobile_number");
                            String pin = object.getString("code_number");
                            String status = object.getString("status");
                            String userName = object.getString("name");
                            String shopeORCompanyName = object.getString("shopeORCompanyName");
                            String address = object.getString("address");
                            String location = object.getString("location");
                            if (phn.equals(phone) && pin.equals(pin_number)) {
                                editor.putString(Common.PHONE_NUMBER, phn);
                                editor.putString(Common.CODE_NUMBER, pin);
                                editor.putString(Common.USER_NAME, userName);
                                editor.putString(Common.SCNAME, shopeORCompanyName);
                                editor.putString(Common.USER_Location, location);
                                editor.commit();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Phone Number and Password Incorrect.", Toast.LENGTH_LONG).show();
                            }
                        }
                        //progressBar.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(LoginActivity.this, "Please Sign Up First.", Toast.LENGTH_LONG).show();
                        etLoginPhone.setText("");
                        etLoginPassword.setText("");
                        progressBar.setVisibility(View.GONE);
                    }

                    Log.d("OBJECT_ERROR", String.valueOf(object2.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSON_ERROR", e.toString());
                    progressBar.setVisibility(View.GONE);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY_ERROR", error.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
        mRequestQueue.add(request);
    }


    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}
