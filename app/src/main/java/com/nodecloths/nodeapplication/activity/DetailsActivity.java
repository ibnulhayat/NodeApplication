package com.nodecloths.nodeapplication.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.helper.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private String p_id;
    private String phn_num;
    private String title;
    private String brand;
    private String fabType;
    private String quantity;
    private String size;
    private String price;
    private String location;
    private String date;
    private String message;
    private String postType;
    private ArrayList<String> imgList;

    private String item_name;
    private String fab_name;
    private String fab_type;
    private String gsm;
    private String design;
    private String measurement_typ;

    private ImageView ivImage;
    private ImageView ivLeftArrow;
    private ImageView ivRightArrow;
    private TextView tvDownward;
    private LinearLayout layoutStockLot;
    private TextView tvDetailsTitle;
    private TextView tvDetailsBrand;
    private TextView tvDetailsFabType;
    private TextView tvDetailsQuantity;
    private TextView tvDetailsLocation;
    private TextView tvDetailsPrice;
    private TextView tvDetailsDate;
    private TextView tvDetailsSize;
    private TextView tvDetailsMassage;
    private TextView tvDetailsNumber;
    private ImageView ivDetailsCall;


    private LinearLayout layoutQuote;
    private TextView tvDetailsItemName;
    private TextView tvDetailsFName;
    private TextView tvDetailsFType;
    private TextView tvDetailsGsm;
    private TextView tvDetailsDesign;
    private TextView tvDetailsQuoteQuantity;
    private TextView tvDetailsMeasType;
    private TextView tvDetailsQuoteSize;
    private TextView tvDetailsQuoteMassage;
    private TextView tvDetailsQuoteNumber;
    private ImageView ivCall;

    private int count = 0;
    private String imgUrl = "";
    private static final int PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initComponent();

        String fragType = getIntent().getStringExtra("type");
        if (fragType.equals("stockLot")) {
            p_id = getIntent().getStringExtra("p_ID");
            phn_num = getIntent().getStringExtra("phn_num");
            title = getIntent().getStringExtra("title");
            brand = getIntent().getStringExtra("brand");
            fabType = getIntent().getStringExtra("fabType");
            quantity = getIntent().getStringExtra("quantity");
            price = getIntent().getStringExtra("price");
            location = getIntent().getStringExtra("location");
            date = getIntent().getStringExtra("date");
            size = getIntent().getStringExtra("size");
            message = getIntent().getStringExtra("message");
            imgList = getIntent().getStringArrayListExtra("list");
            postType = getIntent().getStringExtra("postType");
            layoutStockLot.setVisibility(View.VISIBLE);

            stockLot();
            if(imgList.size() != 0){
                imgeOperation();
            }else {
                noImage();
            }
        }else {
            p_id = getIntent().getStringExtra("p_ID");
            phn_num = getIntent().getStringExtra("phn_num");
            item_name = getIntent().getStringExtra("item_name");
            fab_name = getIntent().getStringExtra("fab_name");
            fab_type = getIntent().getStringExtra("fab_type");
            gsm = getIntent().getStringExtra("gsm");
            design = getIntent().getStringExtra("design");
            quantity = getIntent().getStringExtra("quantity");
            size = getIntent().getStringExtra("size");
            measurement_typ = getIntent().getStringExtra("measurement_typ");
            message = getIntent().getStringExtra("message");
            imgList = getIntent().getStringArrayListExtra("QuoteList");
            postType = getIntent().getStringExtra("postType");
            layoutQuote.setVisibility(View.VISIBLE);
            quote();
            if (imgList.size() != 0){
                imgeOperation();
            }else {
                noImage();
            }

        }
        if (postType.equals("own")){
            ivDetailsCall.setVisibility(View.INVISIBLE);
            ivCall.setVisibility(View.INVISIBLE);
        }else {
            ivDetailsCall.setVisibility(View.VISIBLE);
            ivCall.setVisibility(View.VISIBLE);
        }

    }

    private void stockLot() {
        tvDetailsTitle.setText(title);
        tvDetailsBrand.setText(brand);
        tvDetailsFabType.setText(fabType);
        tvDetailsQuantity.setText(quantity+" Pieces");
        tvDetailsLocation.setText(location);
        tvDetailsPrice.setText(price+" TK");
        tvDetailsDate.setText(date);
        tvDetailsSize.setText(size);
        tvDetailsMassage.setText(message);
        tvDetailsNumber.setText(phn_num);
        ivDetailsCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phn_num));
                startActivity(callIntent);
            }
        });

    }


    private void quote() {
        tvDetailsItemName.setText(item_name);
        tvDetailsFName.setText(fab_name);
        tvDetailsFabType.setText(fab_type);
        tvDetailsGsm.setText(gsm);
        tvDetailsDesign.setText(design);
        tvDetailsQuoteQuantity.setText(quantity+" Pieces");
        tvDetailsMeasType.setText(measurement_typ);
        tvDetailsQuoteSize.setText(size);
        tvDetailsQuoteMassage.setText(message);
        tvDetailsQuoteNumber.setText(phn_num);

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phn_num));
                startActivity(callIntent);
            }
        });
    }

    private void noImage(){
        ivLeftArrow.setVisibility(View.INVISIBLE);
        ivRightArrow.setVisibility(View.INVISIBLE);
        tvDownward.setVisibility(View.INVISIBLE);
    }

    private void imgeOperation() {
        imgUrl = imgList.get(count);
        Picasso.get().load(Common.baseUrl + imgUrl).placeholder(R.drawable.loading).into(ivImage);
        ivLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 0) {
                    count--;
                    imgUrl = imgList.get(count);
                    Picasso.get().load(Common.baseUrl + imgUrl).placeholder(R.drawable.loading).into(ivImage);
                }

            }
        });
        ivRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count < imgList.size()-1) {
                    count++;
                    imgUrl = imgList.get(count);
                    Picasso.get().load(Common.baseUrl + imgUrl).placeholder(R.drawable.loading).into(ivImage);
                }

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
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,System.currentTimeMillis()+".jpeg");
        DownloadManager manager =(DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }
    private void initComponent() {
        ivImage = findViewById(R.id.ivImage);
        ivLeftArrow = findViewById(R.id.ivLeftArrow);
        ivRightArrow = findViewById(R.id.ivRightArrow);
        tvDownward = findViewById(R.id.tvDownward);
        // for stock lot
        layoutStockLot = findViewById(R.id.layoutStockLot);
        tvDetailsTitle = findViewById(R.id.tvDetailsTitle);
        tvDetailsBrand = findViewById(R.id.tvDetailsBrand);
        tvDetailsFabType = findViewById(R.id.tvDetailsFabType);
        tvDetailsQuantity = findViewById(R.id.tvDetailsQuantity);
        tvDetailsLocation = findViewById(R.id.tvDetailsLocation);
        tvDetailsPrice = findViewById(R.id.tvDetailsPrice);
        tvDetailsDate = findViewById(R.id.tvDetailsDate);
        tvDetailsSize = findViewById(R.id.tvDetailsSize);
        tvDetailsMassage = findViewById(R.id.tvDetailsMassage);
        tvDetailsNumber = findViewById(R.id.tvDetailsNumber);
        ivDetailsCall = findViewById(R.id.ivDetailsCall);

        // for Quote lot
        layoutQuote = findViewById(R.id.layoutQuote);
        tvDetailsItemName = findViewById(R.id.tvDetailsItemName);
        tvDetailsFName = findViewById(R.id.tvDetailsFName);
        tvDetailsFType = findViewById(R.id.tvDetailsFType);
        tvDetailsGsm = findViewById(R.id.tvDetailsGsm);
        tvDetailsDesign = findViewById(R.id.tvDetailsDesign);
        tvDetailsQuoteQuantity = findViewById(R.id.tvDetailsQuoteQuantity);
        tvDetailsMeasType = findViewById(R.id.tvDetailsMeasType);
        tvDetailsQuoteSize = findViewById(R.id.tvDetailsQuoteSize);
        tvDetailsQuoteMassage = findViewById(R.id.tvDetailsQuoteMassage);
        tvDetailsQuoteNumber = findViewById(R.id.tvDetailsQuoteNumber);
        ivCall = findViewById(R.id.ivCall);

    }
}
