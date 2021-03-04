package com.nodecloths.nodeapplication.quote;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.activity.MainActivity;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
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
public class CreateQuoteFragment extends Fragment {
    private EditText etItemName, etFabricName, etFabricType, etGsm, etQuantity, etSize, edMessage;
    private AutoCompleteTextView tvDesign, tvMeasurement;
    private CardView cvUploadImage, cvDesignImages;
    private ImageView ivDesignImg1, ivDesignImg2, ivAddImg;
    private TextView ivSelectImgNumber;
    private Button submitButton;
    private ProgressBar progressBar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());
    private RequestQueue mRequestQueue;
    private String status = "0", subm_date;
    private int isClicked = 0;
    private Bitmap bitmap, bitmap_1, bitmap_2, bitmap_3, bitmap_4;
    private JSONObject jsonObject;
    private String type = "null";
    private String imgname = "null";
    private String imgname_2 = "null";
    private String imgname_3 = "null";
    private String imgname_4 = "null";
    private String design = "null";
    private String measurement = "null";
    private String exten = ".jpg";
    private Uri imageUri;
    public static final int PICK_IMAGE = 1;
    private String itemName, fabName, fabType, gsm, quantity, size, et_message;
    String[] designList = {"Print","Embroidery","Others"};
    String[] measurmentList = {"Asian","African","European","Australian","American"};
    private int count = 0;

    private FirebaseDatabase firebaseDatabase;
    public CreateQuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_quote, container, false);

        initComponents(view);
        mRequestQueue = Volley.newRequestQueue(getContext());
        // text auto complite
        ArrayAdapter<String> designAdapter = new ArrayAdapter<>(getActivity(), R.layout.suggetion_layout, R.id.tvSuggetionId, designList);
        tvDesign.setAdapter(designAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.suggetion_layout, R.id.tvSuggetionId, measurmentList);
        tvMeasurement.setAdapter(adapter);


        cvUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    Common.requestMultiplePermissions(getActivity());
                } else {
                    openGallery();
                }
            }

        });

        ivAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        if (isClicked == 0) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClicked = 1;
                    itemName = etItemName.getText().toString().trim();
                    fabName = etFabricName.getText().toString().trim();
                    fabType = etFabricType.getText().toString().trim();
                    gsm = etGsm.getText().toString().trim();
                    design = tvDesign.getText().toString().trim();
                    quantity = etQuantity.getText().toString().trim();
                    measurement = tvMeasurement.getText().toString().trim();
                    size = etSize.getText().toString().trim();
                    et_message = edMessage.getText().toString().trim();
                    subm_date = dateFormat.format(Calendar.getInstance().getTime());

                    if (itemName.isEmpty()) {
                        etItemName.setError("");
                    }
                    if (fabName.isEmpty()) {
                        etFabricName.setError("");
                    }
                    if (fabType.isEmpty()) {
                        etFabricType.setError("");
                    }
                    if (gsm.isEmpty()) {
                        etGsm.setError("");
                    }
                    if (quantity.isEmpty()) {
                        etQuantity.setError("");
                    }
                    if (size.isEmpty()) {
                        etSize.setError("");
                    }
                    if (et_message.isEmpty()) {
                        edMessage.setError("");
                    }
                    if (!itemName.isEmpty() && !fabName.isEmpty() &&
                            !fabType.isEmpty() && !gsm.isEmpty()
                            && !quantity.isEmpty() && !size.isEmpty() && !et_message.isEmpty()) {
                        if (type.equals("image")) {
                            uploadImage(bitmap_1, imgname);
                        } else {
                            dataPost(exten);
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        submitButton.setTextSize(15);
                    }

                }

            });
        }

        return view;
    }

    private void initComponents(View view) {

        etItemName = view.findViewById(R.id.etItemName);
        etFabricName = view.findViewById(R.id.etFabricName);
        etFabricType = view.findViewById(R.id.etFabricType);
        etGsm = view.findViewById(R.id.etGsm);
        tvDesign = view.findViewById(R.id.tvDesign);
        etQuantity = view.findViewById(R.id.etQuantity);
        tvMeasurement = view.findViewById(R.id.tvMeasurement);
        etSize = view.findViewById(R.id.etSize);
        edMessage = view.findViewById(R.id.edMessage);
        cvUploadImage = view.findViewById(R.id.cvUploadImage);
        cvDesignImages = view.findViewById(R.id.cvDesignImages);
        ivDesignImg1 = view.findViewById(R.id.ivDesignImg1);
        ivDesignImg2 = view.findViewById(R.id.ivDesignImg2);
        ivSelectImgNumber = view.findViewById(R.id.ivSelectImgNumber);
        ivAddImg = view.findViewById(R.id.ivAddImg);
        submitButton = view.findViewById(R.id.submitButton);
        progressBar = view.findViewById(R.id.progressBar);

    }

    private void dataPost(final String ext) {
        submitButton.setText("Data Uploading...");
        StringRequest request = new StringRequest(Request.Method.POST, Apis.quotePost, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (response.equals("Inserted Successfully")) {
                            Toast.makeText(getContext(), "Post Successfully", Toast.LENGTH_SHORT).show();
                            seveOnfirebase();
                            selectFragment = "quote";
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 6000);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                submitButton.setText("Your Net is slow. Plz try aging");
                progressBar.setVisibility(View.INVISIBLE);
                submitButton.setTextSize(15);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("mobile_number", Common.phoneNum);
                MyData.put("item_name", itemName);
                MyData.put("fab_name", fabName);
                MyData.put("fab_type", fabType);
                MyData.put("gsm", gsm);
                MyData.put("design", design);
                MyData.put("quantity", quantity);
                MyData.put("size", size);
                MyData.put("measurement_type", measurement);
                MyData.put("img_1", imgname + ext);
                MyData.put("img_2", imgname_2 + ext);
                MyData.put("img_3", imgname_3 + ext);
                MyData.put("img_4", imgname_4 + ext);
                MyData.put("description", et_message);
                return MyData;
            }
        };

        mRequestQueue.add(request);
    }

    private void seveOnfirebase() {
        final DatabaseReference reference = firebaseDatabase.getReference();
        // firebase data update
        reference.child("triggerApps").setValue(random());
        reference.child("fab_type").setValue("Fab: "+fabName+", "+"Quantity: "+quantity+" piss");
        reference.child("fab_name").setValue(itemName+" Item is available.");
        reference.child("userType").setValue("QUote");
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


            count++;
            if (count == 1) {
                bitmap_1 = loadedBitmap;
                cvUploadImage.setVisibility(View.GONE);
                imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
                ivDesignImg1.setImageBitmap(bitmap_1);
            } else if (count == 2) {
                ivDesignImg2.setVisibility(View.VISIBLE);
                ivSelectImgNumber.setVisibility(View.VISIBLE);
                ivSelectImgNumber.setText(String.valueOf(count));
                bitmap_2 = loadedBitmap;
                imgname_2 = String.valueOf(Calendar.getInstance().getTimeInMillis());
                ivDesignImg2.setImageBitmap(bitmap_2);
                ivSelectImgNumber.setText(String.valueOf(count));
            } else if (count == 3) {
                bitmap_3 = loadedBitmap;
                imgname_3 = String.valueOf(Calendar.getInstance().getTimeInMillis());
                ivSelectImgNumber.setText(String.valueOf(count));
                ivDesignImg2.setImageBitmap(bitmap_3);
            } else {
                bitmap_4 = loadedBitmap;
                ivAddImg.setVisibility(View.GONE);
                imgname_4 = String.valueOf(Calendar.getInstance().getTimeInMillis());
                ivSelectImgNumber.setText(String.valueOf(count));
                ivDesignImg2.setImageBitmap(bitmap_4);
            }

            cvDesignImages.setVisibility(View.VISIBLE);
            type = "image";
        }
    }

    int responce = 1;

    private void uploadImage(Bitmap bitmap, String imageName) {
        submitButton.setText("1st Image Uploading.....");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", imageName);
            jsonObject.put("image", encodedImage);

        } catch (JSONException e) {
            Log.e("JSONObjectHere", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Apis.img_url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        String Strvalue = null;
                        try {
                            Strvalue = jsonObject.getString("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int value = Integer.valueOf(Strvalue);
                        responce = responce + value;
                        if (responce < count + 1) {
                            if (responce == 2) {
                                uploadImage(bitmap_2, imgname_2);
                                submitButton.setText("2nd Image Uploading...");
                            } else if (responce == 3) {
                                uploadImage(bitmap_3, imgname_3);
                                submitButton.setText("3rd Image Uploading...");
                            } else if (responce == 4) {
                                uploadImage(bitmap_4, imgname_4);
                                submitButton.setText("4th Image Uploading...");
                            }
                        } else {
                            dataPost(exten);
                        }
                        Log.d(TAG, responce + " " + " " + count);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, volleyError.toString());
                submitButton.setText("Your Net is slow. Plz try aging");
                progressBar.setVisibility(View.INVISIBLE);
                submitButton.setTextSize(15);
            }
        });
        mRequestQueue.add(jsonObjectRequest);
        mRequestQueue.getCache().clear();

    }

}
