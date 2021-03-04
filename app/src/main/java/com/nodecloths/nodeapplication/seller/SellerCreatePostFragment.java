package com.nodecloths.nodeapplication.seller;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.activity.MainActivity;
import com.nodecloths.nodeapplication.adapter.ColorAdapter;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.ColorsList;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.model.ColorsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.nodecloths.nodeapplication.helper.Common.decodeImageFromFiles;
import static com.nodecloths.nodeapplication.helper.Common.random;
import static com.nodecloths.nodeapplication.helper.Common.selectFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SellerCreatePostFragment extends Fragment {

    private View view;
    private EditText etFabricName,etFabricType,etFabricGsmMin,etFabricGsmMax,etWeightMin,etWeightMax;
    private EditText etSellerLocation;
    private EditText etPrice;
    private EditText edMessage;
    private TextView tvColor;
    private View idDivider;
    private TextView tvSelectImage;
    private ImageView ivSelectImage;
    private CardView cvSelectImage;
    private Button submitButton;
    private ProgressBar progressBar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());
    private RequestQueue mRequestQueue;
    private String status ="0",colorName, haxCode,subm_date;
    private ArrayList<ColorsModel> colorsModels;
    private ColorAdapter colorAdapter;
    private int isClicked = 0;
    private Bitmap bitmap;
    private JSONObject jsonObject;
    private String type = "";
    private String imgname;
    private Uri imageUri;
    public static final int PICK_IMAGE = 1;
    private String et_fab_name,et_fab_type,et_fab_g_min,et_fab_g_max,et_wei_min,et_message,et_location,et_price;


    private FirebaseDatabase firebaseDatabase;
    public SellerCreatePostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_seller_creat_post, container, false);

        initComponents();
        colorsModels = ColorsList.myColor();
        mRequestQueue = Volley.newRequestQueue(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();

        tvColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
                idDivider.setVisibility(View.GONE);
                tvSelectImage.setVisibility(View.GONE);
                type = "color";
            }

        });

        tvSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    requestMultiplePermissions();
                } else {
                    openGallery();
                }
                idDivider.setVisibility(View.GONE);
                tvColor.setVisibility(View.GONE);
            }

        });

        if (isClicked == 0) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClicked = 1;
                     et_fab_name = etFabricName.getText().toString().trim();
                    et_fab_type = etFabricType.getText().toString().trim();
                    et_fab_g_min = etFabricGsmMin.getText().toString().trim();
                    et_fab_g_max = etFabricGsmMax.getText().toString().trim();
                    et_wei_min = etWeightMin.getText().toString().trim();
                    et_message = edMessage.getText().toString().trim();
                    et_location = etSellerLocation.getText().toString().trim();
                    et_price = etPrice.getText().toString().trim();
                    subm_date = dateFormat.format(Calendar.getInstance().getTime());

                    if (et_fab_name.isEmpty()) {
                        etFabricName.setError("");
                    }
                    if (et_fab_type.isEmpty()) {
                        etFabricType.setError("");
                    }
                    if (et_fab_g_min.isEmpty()) {
                        etFabricGsmMin.setError("");
                    }
                    if (et_fab_g_max.isEmpty()) {
                        etFabricGsmMax.setError("");
                    }
                    if (et_wei_min.isEmpty()) {
                        etWeightMin.setError("");
                    }
                    if (et_location.isEmpty()) {
                        etSellerLocation.setError("");
                    }
                    if (et_price.isEmpty()) {
                        etPrice.setError("");
                    }
                    if (et_message.isEmpty()) {
                        edMessage.setError("");
                    } else if (!et_fab_type.isEmpty() && !et_fab_g_min.isEmpty() &&
                            !et_wei_min.isEmpty()  && !et_message.isEmpty()
                            && !et_location.isEmpty() && !et_price.isEmpty()) {
                        if (type.equals("image")){
                            uploadImage(bitmap);
                        }else {
                            dataPost(et_fab_name, et_fab_type, et_fab_g_min, et_fab_g_max, et_wei_min
                                    , et_message, et_location,et_price,colorName,haxCode,type);
                        }
                        submitButton.setText("Data Uploading...");
                        progressBar.setVisibility(View.VISIBLE);
                        submitButton.setTextSize(15);
                    }

                }

            });
        }


        return view;
    }
    private void initComponents() {

        etFabricName = view.findViewById(R.id.etFabricName);
        etFabricType = view.findViewById(R.id.etFabricType);
        etFabricGsmMin = view.findViewById(R.id.etFabricGsmMin);
        etFabricGsmMax = view.findViewById(R.id.etFabricGsmMax);
        etWeightMin = view.findViewById(R.id.etWeightMin);
        etWeightMax = view.findViewById(R.id.etWeightMax);
        etSellerLocation = view.findViewById(R.id.etSellerLocation);
        etPrice = view.findViewById(R.id.etPrice);
        tvColor = view.findViewById(R.id.tvColor);
        idDivider = view.findViewById(R.id.idDivider);
        tvSelectImage = view.findViewById(R.id.tvSelectImage);
        ivSelectImage = view.findViewById(R.id.ivSelectImage);
        cvSelectImage = view.findViewById(R.id.cvSelectImage);
        edMessage = view.findViewById(R.id.edMessage);
        submitButton = view.findViewById(R.id.submitButton);
        progressBar = view.findViewById(R.id.progressBar);

    }


    private void dataPost(final String fab_name, final String fab_type, final String fab_g_min, final String fab_g_max,
                          final String wei_min,  final String message, final String location, final String price,
                          final String colorNAME, final String haxCODE,final String imageName){

        StringRequest request = new StringRequest(Request.Method.POST, Apis.sellerpostItem, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        if (response.equals("Inserted Successfully")){
                            Toast.makeText(getContext(), "Post Successfully", Toast.LENGTH_SHORT).show();
                            seveOnfirebase();
                            selectFragment = "fabric";
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }
                    }}, 6000);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("mobile_number", Common.phoneNum); //Add the data you'd like to send to the server.
                MyData.put("post_status", status);
                MyData.put("seller_name", Common.p_name);
                MyData.put("fabric_name", fab_name);
                MyData.put("fabric_type", fab_type);
                MyData.put("fabric_gsm_min", fab_g_min);
                MyData.put("fabric_gsm_max", fab_g_max);
                MyData.put("weight_min", wei_min);
                MyData.put("image", imageName);
                MyData.put("fabric_color", colorNAME);
                MyData.put("fabric_color_pantone", haxCODE);
                MyData.put("seller_location", location);
                MyData.put("date_and_time", subm_date);
                MyData.put("additional_massage", message);
                MyData.put("extra_one", price);
                MyData.put("extra_two", "0");
                MyData.put("extra_three", "0");
                return MyData;
            }
        };

        mRequestQueue.add(request);
    }

    private void seveOnfirebase(){
        final DatabaseReference reference = firebaseDatabase.getReference();
        // firebase data update
        reference.child("triggerApps").setValue(random());
        reference.child("fab_type").setValue("Fab: "+et_fab_type+", "+"Price: "+et_price+" tk/kg"+", Location: "+et_location);
        reference.child("fab_name").setValue(et_fab_name+" fabric is available.");
        reference.child("userType").setValue("FAbrics");
    }

    private void openColorPicker() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.color_picker_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        RecyclerView recyclerView = dialog.findViewById(R.id.colorRecyclerView);
        EditText etColorName = dialog.findViewById(R.id.etColorName);
        etColorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchData(s.toString());

            }
        });
        colorAdapter = new ColorAdapter(getContext(), colorsModels, new ColorAdapter.RecyclerViewClickLister() {
            @Override
            public void onClick(View view, int position, ArrayList<ColorsModel> list) {
                colorName = list.get(position).getColorName();
                haxCode = list.get(position).getHexColorCode();
                Toast.makeText(getContext(), ""+colorName, Toast.LENGTH_SHORT).show();
                tvColor.setBackgroundColor(Color.parseColor(haxCode));
                dialog.dismiss();
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        recyclerView.setAdapter(colorAdapter);
        colorAdapter.notifyDataSetChanged();
        dialog.show();
    }


    //----------For search-----------
    public void searchData(String text) {
        final List<ColorsModel> searchName = new ArrayList<>();
        searchName.clear();
        for (ColorsModel model : colorsModels) {
            if (model.getColorName().toLowerCase().contains(text.toLowerCase())) {

                searchName.add(model);
            }
        }
        colorAdapter.filter(searchName);
    }


    private void requestMultiplePermissions() {
        Dexter.withActivity(getActivity())
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            Toast.makeText(getContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();

            Cursor cursor = MediaStore.Images.Media.query(getActivity().getContentResolver(), imageUri, new String[]{MediaStore.Images.Media.DATA});

            if (cursor != null && cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //Create ImageCompressTask and execute with Executor.
                bitmap = decodeImageFromFiles(path, /* your desired width*/300, /*your desired height*/ 300);
            }
            ivSelectImage.setImageBitmap(bitmap);
            cvSelectImage.setVisibility(View.VISIBLE);
            type = "image";
            imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            Log.d("IMAGEURL", String.valueOf("" + bitmap));
        }
    }




    private void uploadImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", imgname);
            jsonObject.put("image", encodedImage);

        } catch (JSONException e) {
            Log.e("JSONObjectHere", e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Apis.img_url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("AAAAAAAA", jsonObject.toString());
                        if (jsonObject.toString().toLowerCase().contains("1")) {
                            dataPost(et_fab_name, et_fab_type, et_fab_g_min, et_fab_g_max, et_wei_min
                                    ,  et_message,et_location,et_price,type,"#000000",imgname+".jpg");
                        }
                        mRequestQueue.getCache().clear();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("BBBBBBBBB", volleyError.toString());
                submitButton.setText("Timeout.Your Net is slow. Plz try aging");
                progressBar.setVisibility(View.INVISIBLE);
                submitButton.setTextSize(15);
            }
        });
        mRequestQueue.add(jsonObjectRequest);

    }

}
