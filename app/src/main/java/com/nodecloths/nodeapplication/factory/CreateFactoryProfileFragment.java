package com.nodecloths.nodeapplication.factory;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.nodecloths.nodeapplication.helper.Common.TAG;
import static com.nodecloths.nodeapplication.helper.Common.decodeImageFromFiles;
import static com.nodecloths.nodeapplication.helper.Common.random;
import static com.nodecloths.nodeapplication.helper.Common.rotateBitmap;
import static com.nodecloths.nodeapplication.helper.Common.selectFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFactoryProfileFragment extends Fragment {
    private EditText etFactoryName,etSpecificType,etCompliance,etProduction,etOrder;
    private Spinner spFactoryType;
    private EditText etFactoryLocation;
    private EditText edMessage;
    private CardView cvFactory;
    private ImageView ivFactoryImage;
    private TextView tvFactoryImage;
    private Button submitButton;
    private ProgressBar progressBar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());
    private RequestQueue mRequestQueue;
    private String status ="0", subm_date;
    private int isClicked = 0;
    private Bitmap bitmap;
    private JSONObject jsonObject;
    private String type = "null";
    private String imgname = "null";
    private Uri imageUri;
    public static final int PICK_IMAGE = 1;
    private String et_fac_name,et_fac_type,et_spc_type,et_Compliance,et_production,et_order,et_location,et_message;

    private FirebaseDatabase firebaseDatabase;
    public CreateFactoryProfileFragment() {
        // Required empty public constructor
    }
    private String[] facType = {"Select Factory Type","Garments","Knitting","Knitting & Dyeing","Yarn manufacturing","Dyeing","Print","Embroidery","Accessories manufacturing","Sweater","Spinning","Coposit"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_factory_profile, container, false);

        initComponents(view);
        mRequestQueue = Volley.newRequestQueue(getContext());

        tvFactoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    Common.requestMultiplePermissions(getActivity());
                } else {
                    openGallery();
                }
            }

        });

        final List<String> plantsList = new ArrayList<>(Arrays.asList(facType));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.support_simple_spinner_dropdown_item, plantsList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spFactoryType.setAdapter(spinnerArrayAdapter);
        spFactoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Factory Type")) {
                    // nothing do
                } else {
                    et_fac_type = parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (isClicked == 0) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClicked = 1;
                    et_fac_name = etFactoryName.getText().toString().trim();

                    et_spc_type = etSpecificType.getText().toString().trim();
                    et_Compliance = etCompliance.getText().toString().trim();
                    et_production = etProduction.getText().toString().trim();
                    et_order = etOrder.getText().toString().trim();
                    et_location = etFactoryLocation.getText().toString().trim();
                    et_message = edMessage.getText().toString().trim();
                    subm_date = dateFormat.format(Calendar.getInstance().getTime());

                    if (et_fac_name.isEmpty()) {
                        etFactoryName.setError("");
                    }

                    if (et_spc_type.isEmpty()) {
                        etSpecificType.setError("");
                    }
                    if (et_Compliance.isEmpty()) {
                        etCompliance.setError("");
                    }
                    if (et_production.isEmpty()) {
                        etProduction.setError("");
                    }
                    if (et_order.isEmpty()) {
                        etOrder.setError("");
                    }
                    if (et_location.isEmpty()) {
                        etFactoryLocation.setError("");
                    }
                    if (et_message.isEmpty()) {
                        edMessage.setError("");
                    }
                    if (!et_fac_name.isEmpty() && !et_fac_type.isEmpty() &&
                            !et_spc_type.isEmpty()  && !et_production.isEmpty()
                            && !et_location.isEmpty() && !et_Compliance.isEmpty()&& !et_order.isEmpty()) {
                        if (type.equals("image")){
                            uploadImage(bitmap);
                        }else {
                            dataPost(imgname);
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
    private void initComponents(View view) {

        etFactoryName = view.findViewById(R.id.etFactoryName);
        spFactoryType = view.findViewById(R.id.spFactoryType);
        etSpecificType = view.findViewById(R.id.etSpecificType);
        etCompliance = view.findViewById(R.id.etCompliance);
        etProduction = view.findViewById(R.id.etProduction);
        etOrder = view.findViewById(R.id.etOrder);
        etFactoryLocation = view.findViewById(R.id.etFactoryLocation);
        edMessage = view.findViewById(R.id.edMessage);
        tvFactoryImage = view.findViewById(R.id.tvFactoryImage);
        ivFactoryImage = view.findViewById(R.id.ivFactoryImage);
        cvFactory = view.findViewById(R.id.cvFactory);
        submitButton = view.findViewById(R.id.submitButton);
        progressBar = view.findViewById(R.id.progressBar);

    }

    private void dataPost(final String imgName){

        StringRequest request = new StringRequest(Request.Method.POST, Apis.factoryPost, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        if (response.equals("Inserted Successfully")){
                            Toast.makeText(getContext(), "Post Successfully", Toast.LENGTH_SHORT).show();
                            seveOnfirebase();
                            selectFragment = "factory";
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
                MyData.put("poster_name", Common.p_name);
                MyData.put("factory_name", et_fac_name);
                MyData.put("factory_type", et_fac_type);
                MyData.put("specific_type", et_spc_type);
                MyData.put("compliance_percentage", et_Compliance);
                MyData.put("production_capability", et_production);
                MyData.put("minimum_order", et_order);
                MyData.put("factory_location", et_location);
                MyData.put("factory_description", et_message);
                MyData.put("factory_picture", imgName);
                MyData.put("date_time", subm_date);
                MyData.put("one", "0");
                MyData.put("two", "0");
                MyData.put("three", "0");
                return MyData;
            }
        };

        mRequestQueue.add(request);
    }

    private void seveOnfirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference();
        // firebase data update
        reference.child("triggerApps").setValue(random());
        reference.child("fab_type").setValue("Fab: "+et_fac_type+", "+"Production: "+et_production+" piss"+", Location: "+et_location);
        reference.child("fab_name").setValue(et_fac_name);
        reference.child("userType").setValue("FActory");
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

            // Get selected gallery image
            Uri selectedPicture = data.getData();
            // Get and resize profile image
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedPicture, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            bitmap = decodeImageFromFiles(picturePath, /* your desired width*/720, /*your desired height*/ 1280);

            Bitmap loadedBitmap = bitmap;

                    ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new ExifInterface(pictureFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = ExifInterface.ORIENTATION_NORMAL;

            if (exif != null)
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    loadedBitmap = rotateBitmap(loadedBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    loadedBitmap = rotateBitmap(loadedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    loadedBitmap = rotateBitmap(loadedBitmap, 270);
                    break;
            }
            bitmap = loadedBitmap;
            ivFactoryImage.setImageBitmap(bitmap);
            cvFactory.setVisibility(View.VISIBLE);
            type = "image";
            imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            Log.d("IMAGEURL", String.valueOf(orientation+" "));
            
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

                        if (jsonObject.toString().toLowerCase().contains("1")) {
                            dataPost(imgname+".jpg");
                        }
                        mRequestQueue.getCache().clear();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, volleyError.toString());
                submitButton.setText("Timeout.Your Net is slow. Plz try aging");
                progressBar.setVisibility(View.INVISIBLE);
                submitButton.setTextSize(15);
            }
        });
        mRequestQueue.add(jsonObjectRequest);

    }


}
