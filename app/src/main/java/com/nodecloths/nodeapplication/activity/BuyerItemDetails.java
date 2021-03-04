package com.nodecloths.nodeapplication.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.nodecloths.nodeapplication.helper.SqliteDB;
import com.nodecloths.nodeapplication.model.ApplyRecordLIst;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyerItemDetails extends AppCompatActivity {

    private String p_id;
    private String f_name;
    private String f_type;
    private String f_gsm_min;
    private String f_gsm_max;
    private String w_min;
    private String w_max;
    private String fc_name;
    private String fc_code;
    private String date;
    private String comment;
    private String message;
    private String imgORcolor;
    private String mypost;

    private CardView cvColour;
    private ImageView ivImage;
    private TextView tvDetailsFN;
    private TextView tvDetailsFT;
    private TextView tvDetailsCN;
    private TextView tvDetailsGSM;
    private TextView tvDetailsWeight;
    private TextView tvDetailsDate;
    private TextView tvDetailsMassage;
    private TextView etMessage;
    private Button SubButton;
    private ConstraintLayout layout8;
    private SqliteDB sqliteDB;
    private List<ApplyRecordLIst> applyList;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_item_details);
        initComponent();

        p_id = getIntent().getStringExtra("P_ID");
        f_name = getIntent().getStringExtra("fName");
        f_type = getIntent().getStringExtra("fType");
        f_gsm_min = getIntent().getStringExtra("f_gsm_min");
        f_gsm_max = getIntent().getStringExtra("f_gsm_max");
        w_min = getIntent().getStringExtra("w_min");
        w_max = getIntent().getStringExtra("w_max");
        fc_name = getIntent().getStringExtra("fc_name");
        fc_code = getIntent().getStringExtra("fc_code");
        date = getIntent().getStringExtra("date");
        comment = getIntent().getStringExtra("comment");
        message = getIntent().getStringExtra("message");
        imgORcolor = getIntent().getStringExtra("imgORcolor");
        mypost = getIntent().getStringExtra("mypost");

        if (mypost.equals("1")){
            layout8.setVisibility(View.GONE);
        }else {
            layout8.setVisibility(View.VISIBLE);
        }

        if (fc_name.equals("image")){
            Picasso.get().load(Common.baseUrl+imgORcolor).placeholder(R.drawable.loading).into(ivImage);
        }else {
            cvColour.setCardBackgroundColor(Color.parseColor(fc_code));
            ivImage.setVisibility(View.GONE);
        }

        // data set

        tvDetailsFN.setText(":  " +f_name);
        tvDetailsFT.setText(":  " +f_type);
        tvDetailsGSM.setText(":  " + f_gsm_min + " ~ " + f_gsm_max);
        tvDetailsWeight.setText(":  " + w_min + " ~ " + w_max + "  kg");
        tvDetailsCN.setText(":  " + fc_name);
        tvDetailsDate.setText(":  " +date );
        tvDetailsMassage.setText(":  " +message);

        String bitId = "";
        for (int j = 0; j < applyList.size(); j++) {
            bitId = applyList.get(j).getApplyPost_id();
            if (bitId.equals(p_id)) {
                layout8.setVisibility(View.GONE);
            }
        }

        // submit button on click
        SubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getStatus(p_id).equals("1")) {
                    layout8.setVisibility(View.GONE);
                } else {
                    layout8.setVisibility(View.VISIBLE);
                    showDialog(p_id);
                }
            }
        });
    }

    private String getStatus(String id) {
        String status = "";
        for (int j = 0; j < applyList.size(); j++) {
            String bit_Id = applyList.get(j).getApplyPost_id();
            if (bit_Id.equals(id)){
                status = "1";
            }
        }
        return status;
    }
    private void initComponent() {
        cvColour = findViewById(R.id.cvColour);
        ivImage = findViewById(R.id.ivImage);
        tvDetailsFN = findViewById(R.id.tvDetailsFN);
        tvDetailsFT = findViewById(R.id.tvDetailsFT);
        tvDetailsCN = findViewById(R.id.tvDetailsCN);
        tvDetailsGSM = findViewById(R.id.tvDetailsGSM);
        tvDetailsWeight = findViewById(R.id.tvDetailsWeight);
        tvDetailsDate = findViewById(R.id.tvDetailsDate);
        tvDetailsMassage = findViewById(R.id.tvDetailsMassage);
        layout8 = findViewById(R.id.layout8);
        etMessage = findViewById(R.id.etMessage);
        SubButton = findViewById(R.id.SubButton);

        sqliteDB = new SqliteDB(this);
        applyList = sqliteDB.applyList();

    }


    private void showDialog(final String id) {

        final String p_id = id;
                String mess = etMessage.getText().toString().trim();
                commentPost(p_id, Common.phoneNum, Common.p_name, mess, Common.user_loca);
                etMessage.clearFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etMessage.setShowSoftInputOnFocus(false);
        }
        /** Sql database using for the apply tracking */
                long row = sqliteDB.insertApplyList(p_id, "1");
                if (row == -1) {
                    Toast.makeText(this, "Data not Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    layout8.setVisibility(View.GONE);
                    //Toast.makeText(mContext, "Data Add", Toast.LENGTH_SHORT).show();
                }

    }

    private void commentPost(final String b_num, final String s_num, final String s_name, final String mess, final String s_loca) {
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.getCache().clear();
        StringRequest request = new StringRequest(Request.Method.POST, Apis.comment_post, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (response.equals("Inserted Successfully")) {
                            //Toast.makeText(mContext, "" + response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError", error.getMessage());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("post_id", b_num); //Add the data you'd like to send to the server.
                MyData.put("seller_number", s_num);
                MyData.put("seller_name", s_name);
                MyData.put("fabric_price", mess);
                MyData.put("seller_location", s_loca);

                return MyData;
            }
        };
        mRequestQueue.add(request);
    }


}
