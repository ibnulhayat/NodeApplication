package com.nodecloths.nodeapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText etPhoneNum, etPinNumber, etName, etShopCompanyName, etAddress;
    private Spinner spinnerLocation;
    private Button signUpButton;
    private ProgressBar progressBar;
    private TextView tvSellerLogIn;
    private LinearLayout linearLayout;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.getDefault());
    private RequestQueue mRequestQueue;
    private String[] plants = new String[]{
            "Select Location",
            "NARAYANGANJ",
            "KONABARI",
            "SHODORGHAT",
            "ISLAMPUR",
            "ADABOR",
            "BADDA",
            "BAGERHAT",
            "BANGSHAL",
            "BANANI",
            "BANDARBAN",
            "BARGUNA",
            "BARISAL",
            "BHASAN TEK",
            "BHATARA",
            "BHOLA",
            "BIMAN BANDAR",
            "BOGRA",
            "BRAHMANBARIA",
            "CANTONMENT",
            "CHAK BAZAR",
            "CHANDPUR",
            "CHAPAI NABABGAN",
            "CHITTAGONG",
            "CHUADANGA",
            "COMILLA",
            "COX'S BAZAR",
            "DAKSHINKHAN",
            "DARUS SALAM",
            "DEMRA",
            "DHAMRAI",
            "DHANMONDI",
            "DINAJPUR",
            "DOHAR",
            "FARIDPUR",
            "FENI",
            "GAIBANDHA",
            "GAZIPUR",
            "GENDARIA",
            "GOPALGANJ",
            "GULSHAN",
            "HABIGANJ",
            "HAZARIBAGH",
            "JHALOKATI",
            "JAMALPUR",
            "JATRABARI",
            "JESSORE",
            "JHENAIDAH",
            "JOYPURHAT",
            "KAFRUL",
            "KADAMTALI",
            "KALABAGAN",
            "KAMRANGIR CHAR",
            "KERANIGANJ",
            "KHAGRACHHARI",
            "KHILGAON",
            "KHILKHET",
            "KHULNA",
            "KISHOREGONJ",
            "KOTWALI",
            "KUSHTIA",
            "KURIGRAM",
            "LALBAGH",
            "LALMONIRHAT",
            "LAKSHMIPUR",
            "MADARIPUR",
            "MAGURA",
            "MANIKGANJ",
            "MAULVIBAZAR",
            "MEHERPUR",
            "MIRPUR",
            "MOHAMMADPUR",
            "MOTIJHEEL",
            "MUGDA PARA",
            "MUNSHIGANJ",
            "MYMENSING",
            "NAOGAON",
            "NARSINGDI",
            "NARAIL",
            "NATORE",
            "NAWABGANJ",
            "NETRAKONA",
            "NEW MARKET",
            "NILPHAMARI",
            "NOAKHALI",
            "PABNA",
            "PALLABI",
            "PALTAN",
            "PANCHAGARH",
            "PATUAKHALI",
            "PIROJPUR",
            "RAJSHAHI",
            "RAMNA",
            "RAMPURA",
            "RANGAMATI",
            "RANGPUR",
            "SABUJBAGH",
            "RAJBARI",
            "SIRAJGANJ",
            "RUPNAGAR",
            "SAVAR",
            "SATKHIRA",
            "SHAHJAHANPUR",
            "SHAH ALI",
            "SHAHBAGH",
            "SHARIATPUR",
            "SHERPUR",
            "SHYAMPUR",
            "SHER-E-BANGLA NAGAR",
            "SUNAMGANJ",
            "SUTRAPUR",
            "SYLHET",
            "TANGAIL",
            "TEJGAON",
            "TEJGAON IND. AREA",
            "THAKURGAON",
            "TURAG",
            "UTTARA PASCHIM",
            "UTTARA PURBA",
            "UTTAR KHAN",
            "WARI",
    };
    private String item = "";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initComponents();
        mRequestQueue = Volley.newRequestQueue(this);
/** SharedPreferences */
        sharedPreferences = getSharedPreferences(Common.MYPRA_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, plantsList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(spinnerArrayAdapter);
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Location")) {
                    // nothing do
                } else {
                    item = parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = etPhoneNum.getText().toString().trim();
                String pinNumber = etPinNumber.getText().toString();
                String name = etName.getText().toString();
                String scName = etShopCompanyName.getText().toString();
                String sellerAddress = etAddress.getText().toString();
                String submi_date = dateFormat.format(Calendar.getInstance().getTime());
                if (phoneNum.isEmpty()) {
                    etPhoneNum.setError("");
                }
                if (pinNumber.isEmpty() || pinNumber.length() > 4) {
                    etPinNumber.setError("");
                }
                if (name.isEmpty()) {
                    etName.setError("");
                }
                if (scName.isEmpty()) {
                    etShopCompanyName.setError("");
                }
                if (sellerAddress.isEmpty()) {
                    etAddress.setError("");
                }


                if (pinNumber.length() > 4) {
                    Toast.makeText(SignupActivity.this, "Code number is large.", Toast.LENGTH_SHORT).show();
                } else if (!phoneNum.isEmpty() && !pinNumber.isEmpty() && !name.isEmpty() &&
                        !scName.isEmpty() && !sellerAddress.isEmpty() && !item.isEmpty()) {
                    dataPost(phoneNum, pinNumber, name, scName, sellerAddress, submi_date, item);
                    progressBar.setVisibility(View.VISIBLE);
                }

                hideKeyboard(v);

            }

        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        tvSellerLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
    }

    private void initComponents() {

        etPhoneNum = findViewById(R.id.etPhoneNum);
        etPinNumber = findViewById(R.id.etPinNumber);
        etName = findViewById(R.id.etName);
        etShopCompanyName = findViewById(R.id.etShopCompanyName);
        etAddress = findViewById(R.id.etAddress);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        signUpButton = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.progressBar);
        tvSellerLogIn = findViewById(R.id.tvSellerLogIn);
        linearLayout = findViewById(R.id.linearLayout);
    }

    @Override
    public void onBackPressed() {
        refresh();
        super.onBackPressed();
    }


    private void dataPost(final String phn_num, final String code, final String name, final String scName,
                          final String Addr,final String e_date, final String location) {

        mRequestQueue.getCache().clear();
        StringRequest request = new StringRequest(Request.Method.POST, Apis.signUp, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (response.equals("Inserted Successfully")) {
                            goToMain();
                            editor.putString(Common.PHONE_NUMBER, phn_num);
                            editor.putString(Common.CODE_NUMBER, code);
                            editor.commit();
                            //Toast.makeText(SignupActivity.this, "Please Login Now", Toast.LENGTH_LONG).show();

                        }else {
                            Toast.makeText(SignupActivity.this, response, Toast.LENGTH_LONG).show();
                        }
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("mobile_number", phn_num);
                MyData.put("code_number", code);
                MyData.put("status", "0");
                MyData.put("name", name);
                MyData.put("shopeORCompanyName", scName);
                MyData.put("address", Addr);
                MyData.put("submission_date", e_date);
                MyData.put("location", location);
                return MyData;
            }
        };

        mRequestQueue.add(request);
    }

    private void refresh() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void goToMain() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
