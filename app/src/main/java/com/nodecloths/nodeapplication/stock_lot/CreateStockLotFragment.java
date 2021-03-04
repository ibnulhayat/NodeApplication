package com.nodecloths.nodeapplication.stock_lot;


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
public class CreateStockLotFragment extends Fragment {
    private EditText etTitle, etBrandType, etFabricType, etTotalQuantity, etPriseParPiss, etLocation, etSize, edMessage;
    private CardView cvUploadImage, cvDisplay, cvOtherImage;
    private ImageView ivDisplayImage, ivSelectImg1, ivSelectImg2, ivAddImg;
    private TextView ivSelectImgNumber, tvSelectImage;
    private Button submitButton;
    private ProgressBar progressBar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy/hh:mm a", Locale.getDefault());
    private RequestQueue mRequestQueue;
    private String status = "0", subm_date;
    private int isClicked = 0;
    private Bitmap bitmap, bigImg, bitmap_1, bitmap_2, bitmap_3, bitmap_4, bitmap_5, bitmap_6, bitmap_7, bitmap_8;
    private JSONObject jsonObject;
    private String type = "null";
    private String imgname_1 = "null";
    private String imgname_2 = "null";
    private String imgname_3 = "null";
    private String imgname_4 = "null";
    private String imgname_5 = "null";
    private String imgname_6 = "null";
    private String imgname_7 = "null";
    private String imgname_8 = "null";
    private String big_img = "null";
    private String exten = ".jpg";
    private Uri imageUri;
    public static final int PICK_IMAGE = 1;
    private String title, brandType, fabType, quantity, prise, location, size, et_message;
    private int count = 0;
    private String disImg = "";

    private FirebaseDatabase firebaseDatabase;
    public CreateStockLotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_stock_lot, container, false);

        initComponents(view);
        mRequestQueue = Volley.newRequestQueue(getContext());

        tvSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    Common.requestMultiplePermissions(getActivity());
                } else {
                    openGallery();
                    disImg = "dimg";

                }
            }

        });

        cvUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                disImg = "other";
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
                    title = etTitle.getText().toString().trim();
                    brandType = etBrandType.getText().toString().trim();
                    fabType = etFabricType.getText().toString().trim();
                    quantity = etTotalQuantity.getText().toString().trim();
                    prise = etPriseParPiss.getText().toString().trim();
                    location = etLocation.getText().toString().trim();
                    size = etSize.getText().toString().trim();
                    et_message = edMessage.getText().toString().trim();
                    subm_date = dateFormat.format(Calendar.getInstance().getTime());

                    if (title.isEmpty()) {
                        etTitle.setError("");
                    }
                    if (brandType.isEmpty()) {
                        etBrandType.setError("");
                    }
                    if (fabType.isEmpty()) {
                        etFabricType.setError("");
                    }
                    if (prise.isEmpty()) {
                        etPriseParPiss.setError("");
                    }
                    if (quantity.isEmpty()) {
                        etTotalQuantity.setError("");
                    }
                    if (location.isEmpty()) {
                        etLocation.setError("");
                    }
                    if (size.isEmpty()) {
                        etSize.setError("");
                    }
                    if (et_message.isEmpty()) {
                        edMessage.setError("");
                    }
                    if (!title.isEmpty() && !brandType.isEmpty() &&
                            !fabType.isEmpty() && !prise.isEmpty()
                            && !quantity.isEmpty() && !location.isEmpty() && !size.isEmpty() && !et_message.isEmpty()) {
                        if (type.equals("image")) {
                            uploadImage(bigImg, big_img);
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

        etTitle = view.findViewById(R.id.etTitle);
        etBrandType = view.findViewById(R.id.etBrandType);
        etFabricType = view.findViewById(R.id.etFabricType);
        etTotalQuantity = view.findViewById(R.id.etTotalQuantity);
        etPriseParPiss = view.findViewById(R.id.etPriseParPiss);
        etLocation = view.findViewById(R.id.etLocation);
        etSize = view.findViewById(R.id.etSize);
        edMessage = view.findViewById(R.id.edMessage);
        cvDisplay = view.findViewById(R.id.cvDisplay);
        ivDisplayImage = view.findViewById(R.id.ivDisplayImage);
        tvSelectImage = view.findViewById(R.id.tvSelectImage);
        cvUploadImage = view.findViewById(R.id.cvUploadImage);
        cvOtherImage = view.findViewById(R.id.cvOtherImage);
        ivSelectImg1 = view.findViewById(R.id.ivSelectImg1);
        ivSelectImg2 = view.findViewById(R.id.ivSelectImg2);
        ivSelectImgNumber = view.findViewById(R.id.ivSelectImgNumber);
        ivAddImg = view.findViewById(R.id.ivAddImg);
        submitButton = view.findViewById(R.id.submitButton);
        progressBar = view.findViewById(R.id.progressBar);

    }


    private void dataPost(final String ext) {
        submitButton.setText("Data Uploading...");
        StringRequest request = new StringRequest(Request.Method.POST, Apis.stokLotPost, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (response.equals("Inserted Successfully")) {
                            Toast.makeText(getContext(), "Post Successfully", Toast.LENGTH_SHORT).show();
                            seveOnfirebase();
                            selectFragment = "stockLot";
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 4000);


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
                MyData.put("title", title);
                MyData.put("brand", brandType);
                MyData.put("size", size);
                MyData.put("fabrics", fabType);
                MyData.put("quantity", quantity);
                MyData.put("price", prise);
                MyData.put("location", location);
                MyData.put("description", et_message);
                MyData.put("timeDate", subm_date);
                MyData.put("big_img", big_img + ext);
                MyData.put("img_1", imgname_1 + ext);
                MyData.put("img_2", imgname_2 + ext);
                MyData.put("img_3", imgname_3 + ext);
                MyData.put("img_4", imgname_4 + ext);
                MyData.put("img_5", imgname_5 + ext);
                MyData.put("img_6", imgname_6 + ext);
                MyData.put("img_7", imgname_7 + ext);
                MyData.put("img_8", imgname_8 + ext);
                return MyData;
            }
        };

        mRequestQueue.add(request);
    }


    private void seveOnfirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference();
        // firebase data update
        reference.child("triggerApps").setValue(random());
        reference.child("fab_type").setValue("Fab: "+fabType+", "+"Price: "+prise+" par piss"+", Location: "+location);
        reference.child("fab_name").setValue(title+" item is available.");
        reference.child("userType").setValue("STockLot");
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


            if (disImg.equals("dimg")) {
                bigImg = loadedBitmap;
                cvDisplay.setVisibility(View.VISIBLE);
                big_img = String.valueOf(Calendar.getInstance().getTimeInMillis());
                ivDisplayImage.setImageBitmap(bigImg);
            } else {

                count++;
                if (count == 1) {
                    cvUploadImage.setVisibility(View.GONE);
                    cvOtherImage.setVisibility(View.VISIBLE);
                    bitmap_1 = loadedBitmap;
                    imgname_1 = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    ivSelectImg1.setImageBitmap(bitmap_1);
                } else if (count == 2) {
                    bitmap_2 = loadedBitmap;
                    imgname_2 = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    ivSelectImg2.setImageBitmap(bitmap_2);
                    ivSelectImg2.setVisibility(View.VISIBLE);
                    ivSelectImgNumber.setText(String.valueOf(count));
                } else if (count == 3) {
                    bitmap_3 = loadedBitmap;
                    imgname_3 = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    ivSelectImgNumber.setText(String.valueOf(count));
                    ivSelectImg2.setImageBitmap(bitmap_3);
                } else if (count == 4) {
                    bitmap_4 = loadedBitmap;
                    imgname_4 = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    ivSelectImgNumber.setText(String.valueOf(count));
                    ivSelectImg2.setImageBitmap(bitmap_4);
                } else if (count == 5) {
                    bitmap_5 = loadedBitmap;
                    imgname_5 = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    ivSelectImgNumber.setText(String.valueOf(count));
                    ivSelectImg2.setImageBitmap(bitmap_5);
                } else if (count == 6) {
                    bitmap_6 = loadedBitmap;
                    imgname_6 = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    ivSelectImgNumber.setText(String.valueOf(count));
                    ivSelectImg2.setImageBitmap(bitmap_6);
                } else if (count == 7) {
                    bitmap_7 = loadedBitmap;
                    imgname_7 = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    ivSelectImgNumber.setText(String.valueOf(count));
                    ivSelectImg2.setImageBitmap(bitmap_7);
                } else {
                    bitmap_8 = loadedBitmap;
                    ivAddImg.setVisibility(View.GONE);
                    imgname_8 = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    ivSelectImgNumber.setText(String.valueOf(count));
                    ivSelectImg2.setImageBitmap(bitmap_8);
                }
            }
            type = "image";
        }
    }

    int responce = 0;

    private void uploadImage(Bitmap bitmap, String imageName) {
        submitButton.setText("Display Image Uploading.....");
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
                            if (responce == 1) {
                                uploadImage(bitmap_1, imgname_1);
                                submitButton.setText(responce + "st Image Uploading...");
                            } else if (responce == 2) {
                                uploadImage(bitmap_2, imgname_2);
                                submitButton.setText(responce + "nd Image Uploading...");
                            } else if (responce == 3) {
                                uploadImage(bitmap_3, imgname_3);
                                submitButton.setText(responce + "rd Image Uploading...");
                            } else if (responce == 4) {
                                uploadImage(bitmap_4, imgname_4);
                                submitButton.setText(responce + "th Image Uploading...");
                            } else if (responce == 5) {
                                uploadImage(bitmap_5, imgname_5);
                                submitButton.setText(responce + "th Image Uploading...");
                            } else if (responce == 6) {
                                uploadImage(bitmap_6, imgname_6);
                                submitButton.setText(responce + "th Image Uploading...");
                            } else if (responce == 7) {
                                uploadImage(bitmap_7, imgname_7);
                                submitButton.setText(responce + "th Image Uploading...");
                            } else if (responce == 8) {
                                uploadImage(bitmap_8, imgname_8);
                                submitButton.setText(responce + "th Image Uploading...");
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
