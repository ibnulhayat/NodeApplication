package com.nodecloths.nodeapplication.helper;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nodecloths.nodeapplication.activity.MainActivity;

import java.util.List;
import java.util.Random;

public class Common {

    public static String MYPRA_NAME = "com.nodecloths.nodeapplication";
    public static String PHONE_NUMBER = "phone";
    public static String CODE_NUMBER = "code";
    public static String USER_NAME = "username";
    public static String USER_TYPE = "usertype";
    public static String USER_Location = "userLocation";
    public static String SCNAME = "shopeORCompanyName";
    public static String ON_OFF = "OnOff";
    public static String FABRICS = "fabrics";
    public static String STOCKLOT = "stocklot";
    public static String FACTORY = "factory";
    public static String QUOTE = "quote";
    public static String FIRST = "first";
    public static String LAST_POSITION = "LASTPOSITION";

    public static String fab;
    public static String sto;
    public static String fac;
    public static String quo;

    public static String phoneNum;
    public static String p_name;
    public static String user_loca;
    public static String userType;
    public static String baseUrl;
    public static String help_line_num;
    public static String selectFragment;
    public static String TAG = "FRAGMENT_LIFECYCLE";

    public static void setTextOnToolbar(String text){
        //MainActivity.tvToolbar.setText(text);
    }

    private static int MAX_LENGTH = 6;
    private static final String ALLOWED_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm";
    public static String random() {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(MAX_LENGTH);
        for (int i = 0; i < MAX_LENGTH; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static void requestMultiplePermissions(final Context context) {
        Dexter.withActivity((Activity) context)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            Toast.makeText(context, "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public static Bitmap decodeImageFromFiles(String path, int width, int height) {
        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
        scaleOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, scaleOptions);
        int scale = 1;
        while (scaleOptions.outWidth / scale / 2 >= width
                && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2;
        }
        // decode with the sample size
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        outOptions.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, outOptions);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


}
