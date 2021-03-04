package com.nodecloths.nodeapplication.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class SellerItemDetails extends AppCompatActivity {

    private String p_id;
    private String phn_num;
    private String f_name;
    private String f_type;
    private String f_gsm_min;
    private String f_gsm_max;
    private String w_min;
    private String imgORcolor;
    private String fc_name;
    private String fc_code;
    private String price;
    private String location;
    private String date;
    private String message;
    private String mypost;

    private String facName;
    private String facType;
    private String specific;
    private String compliance;
    private String production;
    private String order;
    private String picture;

    private LinearLayout layoutSeller;
    private LinearLayout layoutFactory;
    private CardView cvColour;
    private ImageView ivImage;
    private TextView tvDetailsFN;
    private TextView tvDetailsFT;
    private TextView tvDetailsCN;
    private TextView tvDetailsGSM;
    private TextView tvDetailsWeight;
    private TextView tvDetailsPrice;
    private TextView tvDetailsLocation;
    private TextView tvDetailsDate;
    private TextView tvDetailsMassage;
    private TextView tvDetailsNumber;
    private ImageView ivDetailsCall;
    private TextView tvDownward;

    private TextView tvDetailsFactory;
    private TextView tvDetailsFacType;
    private TextView tvDetailsSpeType;
    private TextView tvDetailsCompliance;
    private TextView tvDetailsProduction;
    private TextView tvDetailsOrder;
    private TextView tvDetailsFactoryLocation;
    private TextView tvDetailsFactoryMassage;
    private TextView tvDetailsFactoryNumber;
    private ImageView ivCall;

    private String imgUrl = "";
    private static final int PERMISSION_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_item_details);
        initComponent();

        String type = getIntent().getStringExtra("type");
        if (type.equals("seller")) {
            p_id = getIntent().getStringExtra("P_ID");
            phn_num = getIntent().getStringExtra("phn_num");
            f_name = getIntent().getStringExtra("fName");
            f_type = getIntent().getStringExtra("fType");
            f_gsm_min = getIntent().getStringExtra("f_gsm_min");
            f_gsm_max = getIntent().getStringExtra("f_gsm_max");
            w_min = getIntent().getStringExtra("w_min");
            imgORcolor = getIntent().getStringExtra("imgORcolor");
            fc_name = getIntent().getStringExtra("fc_name");
            fc_code = getIntent().getStringExtra("fc_code");
            price = getIntent().getStringExtra("price");
            location = getIntent().getStringExtra("location");
            date = getIntent().getStringExtra("date");
            message = getIntent().getStringExtra("message");
            mypost = getIntent().getStringExtra("mypost");
            layoutSeller.setVisibility(View.VISIBLE);
            seller();
        }else {
            p_id = getIntent().getStringExtra("P_ID");
            phn_num = getIntent().getStringExtra("phn_num");
            facName = getIntent().getStringExtra("facName");
            facType = getIntent().getStringExtra("facType");
            specific = getIntent().getStringExtra("specific");
            compliance = getIntent().getStringExtra("compliance");
            production = getIntent().getStringExtra("production");
            order = getIntent().getStringExtra("order");
            location = getIntent().getStringExtra("location");
            picture = getIntent().getStringExtra("picture");
            message = getIntent().getStringExtra("message");
            mypost = getIntent().getStringExtra("mypost");

            if (mypost.equals("own")){
                ivCall.setVisibility(View.GONE);
            }
            layoutFactory.setVisibility(View.VISIBLE);
            factory();
        }


        ivDetailsCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phn_num));
                startActivity(callIntent);
            }
        });
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phn_num));
                startActivity(callIntent);
            }
        });

        tvDownward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        String[] permisssion = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permisssion,PERMISSION_CODE);
                    }else {
                        startDownload();
                    }
                }else {
                    startDownload();
                }
            }
        });


    }


    private void initComponent() {
        // for Seller
        layoutSeller = findViewById(R.id.layoutSeller);
        tvDownward = findViewById(R.id.tvDownward);
        cvColour = findViewById(R.id.cvColour);
        ivImage = findViewById(R.id.ivImage);
        tvDetailsFN = findViewById(R.id.tvDetailsFN);
        tvDetailsFT = findViewById(R.id.tvDetailsFT);
        tvDetailsCN = findViewById(R.id.tvDetailsCN);
        tvDetailsGSM = findViewById(R.id.tvDetailsGSM);
        tvDetailsWeight = findViewById(R.id.tvDetailsWeight);
        tvDetailsPrice = findViewById(R.id.tvDetailsPrice);
        tvDetailsLocation = findViewById(R.id.tvDetailsLocation);
        tvDetailsDate = findViewById(R.id.tvDetailsDate);
        tvDetailsMassage = findViewById(R.id.tvDetailsMassage);
        tvDetailsNumber = findViewById(R.id.tvDetailsNumber);
        ivDetailsCall = findViewById(R.id.ivDetailsCall);

        // For Factory

        layoutFactory = findViewById(R.id.layoutFactory);
        tvDetailsFactory = findViewById(R.id.tvDetailsFactory);
        tvDetailsFacType = findViewById(R.id.tvDetailsFacType);
        tvDetailsSpeType = findViewById(R.id.tvDetailsSpeType);
        tvDetailsCompliance = findViewById(R.id.tvDetailsCompliance);
        tvDetailsProduction = findViewById(R.id.tvDetailsProduction);
        tvDetailsOrder = findViewById(R.id.tvDetailsOrder);
        tvDetailsFactoryLocation = findViewById(R.id.tvDetailsFactoryLocation);
        tvDetailsFactoryMassage = findViewById(R.id.tvDetailsFactoryMassage);
        tvDetailsFactoryNumber = findViewById(R.id.tvDetailsFactoryNumber);
        ivCall = findViewById(R.id.ivCall);
    }

   private void seller(){
        if (mypost.equals("1")) {
            ivDetailsCall.setVisibility(View.INVISIBLE);
        } else {
            ivDetailsCall.setVisibility(View.VISIBLE);
        }

        if (fc_name.equals("image")) {
            imgUrl= Common.baseUrl+imgORcolor;
            ivImage.setVisibility(View.VISIBLE);
            Picasso.get().load(Common.baseUrl + imgORcolor).placeholder(R.drawable.loading).into(ivImage);
        } else {
            tvDownward.setVisibility(View.GONE);
            cvColour.setCardBackgroundColor(Color.parseColor(fc_code));
            ivImage.setVisibility(View.GONE);
        }

       // data set

       tvDetailsFN.setText(f_name);
       tvDetailsFT.setText(f_type);
       tvDetailsCN.setText(fc_name);
       tvDetailsGSM.setText(f_gsm_min + " ~ " + f_gsm_max);
       tvDetailsWeight.setText( w_min + "  kg");
       tvDetailsPrice.setText( price + " tk/kg");
       tvDetailsLocation.setText(location);
       tvDetailsDate.setText(date);
       tvDetailsMassage.setText( message);
       tvDetailsNumber.setText( phn_num);

   }

   private void factory(){

        if (!picture.equals("null")){
            imgUrl= Common.baseUrl+picture;
            Picasso.get().load(Common.baseUrl+picture).placeholder(R.drawable.loading).into(ivImage);
        }else {
            tvDownward.setVisibility(View.GONE);
            ivImage.setImageResource(R.drawable.noimage);
        }

       tvDetailsFactory.setText(facName);
       tvDetailsFacType.setText(facType);
       tvDetailsSpeType.setText(specific);
       tvDetailsCompliance.setText(compliance);
       tvDetailsProduction.setText( production + " Piss");
       tvDetailsOrder.setText( order );
       tvDetailsFactoryLocation.setText(location);
       tvDetailsFactoryMassage.setText( message);
       tvDetailsFactoryNumber.setText( phn_num);

   }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startDownload();
                }else {
                    Toast.makeText(this, "Permisssion Denied....", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startDownload() {
        String name = String.valueOf(System.currentTimeMillis());
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Common.baseUrl+imgUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(name+".jpeg");
        request.setDescription("Image Downloading.....");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,System.currentTimeMillis()+"jpeg");
        DownloadManager manager =(DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}
