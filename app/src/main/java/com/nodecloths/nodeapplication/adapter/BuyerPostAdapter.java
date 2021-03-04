package com.nodecloths.nodeapplication.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.nodecloths.nodeapplication.activity.BuyerItemDetails;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.helper.SqliteDB;
import com.nodecloths.nodeapplication.model.ApplyRecordLIst;
import com.nodecloths.nodeapplication.model.PostItemList;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyerPostAdapter extends RecyclerView.Adapter<BuyerPostAdapter.ViewHolder> {

    private Context mContext;
    private List<PostItemList> postList;
    private List<ApplyRecordLIst> applyList;
    RequestQueue mRequestQueue;
    private SqliteDB sqliteDB;

    public BuyerPostAdapter(Context mContext, List<PostItemList> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @NonNull
    @Override
    public BuyerPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        sqliteDB = new SqliteDB(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.buyer_post_item_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BuyerPostAdapter.ViewHolder viewHolder, final int i) {

        applyList = sqliteDB.applyList();

        PostItemList list = postList.get(i);
        final String p_id = list.getId();
        final String phn_num = list.getPhoneNum();
        final String status = list.getPostStatus();
        final String fName = list.getFabricName();
        final String fType = list.getFabricType();
        final String f_gsm_min = list.getFabric_G_min();
        final String f_gsm_max = list.getFabric_G_max();
        final String w_min = list.getWeight_min();
        final String w_max = list.getWeight_max();
        final String fc_name = list.getFab_colorName();
        final String fc_code = list.getFab_colorCode();
        final String message = list.getMessage();
        final String date = list.getDate();
        final String comment = list.getComment();
        final String imgORcolor = list.getImgORcolor();
        String postType = list.getPostType();
        if (postType.isEmpty()){
            viewHolder.ivDelete.setVisibility(View.INVISIBLE);
            viewHolder.ivEdit.setVisibility(View.INVISIBLE);
        }

        viewHolder.setView(fName, fType, f_gsm_min, f_gsm_max, w_min, w_max, fc_name, fc_code, date, comment,phn_num);

        String bitId = "";
        for (int j = 0; j < applyList.size(); j++) {
            bitId = applyList.get(j).getApplyPost_id();
            if (bitId.equals(p_id)) {
                viewHolder.bidButton.setTextColor(Color.parseColor("#717171"));
            }else {
                viewHolder.bidButton.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }

        viewHolder.bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getStatus(p_id).equals("1")) {
                    Toast.makeText(mContext, "You already comment this post.", Toast.LENGTH_SHORT).show();
                } else {
                    showDialog(p_id);
                }
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BuyerItemDetails.class);
                intent.putExtra("P_ID", p_id);
                intent.putExtra("fName", fName);
                intent.putExtra("fType", fType);
                intent.putExtra("f_gsm_min", f_gsm_min);
                intent.putExtra("f_gsm_max", f_gsm_max);
                intent.putExtra("w_min", w_min);
                intent.putExtra("w_max", w_max);
                intent.putExtra("fc_name", fc_name);
                intent.putExtra("fc_code", fc_code);
                intent.putExtra("date", date);
                intent.putExtra("comment", comment);
                intent.putExtra("message", message);
                intent.putExtra("imgORcolor", imgORcolor);
                intent.putExtra("mypost", "0");
                mContext.startActivity(intent);
            }
        });


        if (!imgORcolor.equals("color")){
            Picasso.get().load(Common.baseUrl+imgORcolor).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    viewHolder.layoutBackImage.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        }else {
            viewHolder.cardView.setBackgroundColor(Color.parseColor(fc_code));
            viewHolder.layoutBackImage.setBackgroundColor(Color.parseColor(fc_code));
        }
    }

    private String getStatus(String id) {
        String status = "";
        for (int j = 0; j < applyList.size(); j++) {
           String bit_Id = applyList.get(j).getApplyPost_id();
           String number = applyList.get(j).getNumber();
            if (bit_Id.equals(id) && Common.phoneNum.equals(number)){
                status = "1";
            }
        }
        return status;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFabricName, tvFabricType, tvFabricGsmMinMax, tvWeightMinMax,
                tvFabricColor, tvDate;
        private Button bidButton;
        private ConstraintLayout etLayout;
        private ConstraintLayout layoutBackImage;
        private ImageView ivEdit, ivDelete;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            layoutBackImage = itemView.findViewById(R.id.layoutBackImage);
            tvFabricName = itemView.findViewById(R.id.tvFabricName);
            tvFabricType = itemView.findViewById(R.id.tvFabricType);
            tvFabricGsmMinMax = itemView.findViewById(R.id.tvFabricGsmMinMax);
            tvWeightMinMax = itemView.findViewById(R.id.tvWeightMinMax);
            tvFabricColor = itemView.findViewById(R.id.tvFabricColor);
            bidButton = itemView.findViewById(R.id.callButton);
            etLayout = itemView.findViewById(R.id.etLayout);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            cardView = itemView.findViewById(R.id.cardView);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        public void setView(String fName, String fType, String f_gsm_min, String f_gsm_max, String w_min, String w_max, String fc_name, String fc_code, String date, String comment,String phn_num) {
            tvFabricName.setText(fName);
            tvFabricType.setText(fType);
            tvFabricGsmMinMax.setText("GSM : " + f_gsm_min + " ~ " + f_gsm_max);
            tvWeightMinMax.setText("WEIGHT: " + w_min + " ~ " + w_max + " kg");
            tvFabricColor.setText("COLOR : " + fc_name);
            tvDate.setText(date + "\n"+ comment+"\n"+phn_num);

            cardView.setBackgroundColor(Color.parseColor(fc_code));
            tvFabricType.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvFabricName.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvFabricGsmMinMax.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvWeightMinMax.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvFabricColor.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvDate.setTextColor(Color.parseColor(invertColor(fc_code)));
            bidButton.setText("Apply");


        }

    }

    public String invertColor(String myColorString) {
        String subString = myColorString.substring(1);
        int color = (int) Long.parseLong(subString, 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        int invertedRed = 255 - r;
        int invertedGreen = 255 - g;
        int invertedBlue = 255 - b;

        int invertedColor = Color.rgb(invertedRed, invertedGreen, invertedBlue);
        String hex = String.format("#%02x%02x%02x", invertedRed, invertedGreen, invertedBlue);
        return hex;
    }

    private void showDialog(final String id) {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.appley_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        final EditText etMessage = dialog.findViewById(R.id.etMessage);
        Button okButton = dialog.findViewById(R.id.okButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        final String p_id = id;

        // dialog ok button
        okButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String mess = etMessage.getText().toString().trim();
                commentPost(p_id, Common.phoneNum, Common.p_name, mess, Common.user_loca);
                dialog.dismiss();
                etMessage.clearFocus();
                etMessage.setShowSoftInputOnFocus(false);

                /** Sql database using for the apply tracking */
                long row = sqliteDB.insertApplyList(p_id, Common.phoneNum);
                if (row == -1) {
                    Toast.makeText(mContext, "Data not Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(mContext, "Data Add", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });

        // dialog cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void commentPost(final String b_num, final String s_num, final String s_name, final String mess, final String s_loca) {
        mRequestQueue = Volley.newRequestQueue(mContext);
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


    //for search
    public void filter(List<PostItemList> searchName) {

        postList = new ArrayList<>();
        postList.addAll(searchName);
        notifyDataSetChanged();
    }
}
