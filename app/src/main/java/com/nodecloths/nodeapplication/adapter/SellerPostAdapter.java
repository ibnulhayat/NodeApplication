package com.nodecloths.nodeapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.activity.SellerItemDetails;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.helper.SqliteDB;
import com.nodecloths.nodeapplication.model.SellerPostItemList;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class SellerPostAdapter extends RecyclerView.Adapter<SellerPostAdapter.ViewHolder> {

    private Context mContext;
    private List<SellerPostItemList> postList;
    RequestQueue mRequestQueue;
    private SqliteDB sqliteDB;

    public SellerPostAdapter(Context mContext, List<SellerPostItemList> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @NonNull
    @Override
    public SellerPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        sqliteDB = new SqliteDB(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.seller_post_item_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SellerPostAdapter.ViewHolder viewHolder, final int i) {


        SellerPostItemList list = postList.get(i);
        final String p_id = list.getId();
        final String phn_num = list.getPhoneNum();
        final String status = list.getPostStatus();
        final String fName = list.getFabricName();
        final String fType = list.getFabricType();
        final String f_gsm_min = list.getFabric_G_min();
        final String f_gsm_max = list.getFabric_G_max();
        final String w_min = list.getWeight_min();
        final String imgORcolor = list.getWeight_max();
        final String fc_name = list.getFab_colorName();
        final String fc_code = list.getFab_colorCode();
        final String loca = list.getLocation();
        final String price = list.getPrice();
        final String message = list.getMessage();
        final String date = list.getDate();
        final String visivility = list.getVisivility();

        viewHolder.setView(fName, fType, f_gsm_min, f_gsm_max, w_min, fc_name, fc_code, date, loca, price,phn_num);

        if (visivility.equals("0")) {
            viewHolder.ivDelete.setVisibility(View.GONE);
            viewHolder.ivEdit.setVisibility(View.GONE);
        }

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

        viewHolder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phn_num));
                mContext.startActivity(callIntent);
                Toast.makeText(mContext, "" + phn_num, Toast.LENGTH_SHORT).show();

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SellerItemDetails.class);
                intent.putExtra("type","seller");
                intent.putExtra("P_ID", p_id);
                intent.putExtra("phn_num", phn_num);
                intent.putExtra("fName", fName);
                intent.putExtra("fType", fType);
                intent.putExtra("f_gsm_min", f_gsm_min);
                intent.putExtra("f_gsm_max", f_gsm_max);
                intent.putExtra("w_min", w_min);
                intent.putExtra("imgORcolor", imgORcolor);
                intent.putExtra("fc_name", fc_name);
                intent.putExtra("fc_code", fc_code);
                intent.putExtra("price", price);
                intent.putExtra("location", loca);
                intent.putExtra("date", date);
                intent.putExtra("message", message);
                intent.putExtra("mypost", "0");
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFabricName, tvFabricType, tvFabricGsmMinMax, tvWeightMinMax,
                tvFabricColor, tvDate, tvLocation, tvPrice;
        private ImageView callButton;
        private ConstraintLayout etLayout;
        private LinearLayout layoutBackImage;
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
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            callButton = itemView.findViewById(R.id.callButton);
            etLayout = itemView.findViewById(R.id.etLayout);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            cardView = itemView.findViewById(R.id.cardView);
            tvDate = itemView.findViewById(R.id.tvDate);

        }

        public void setView(String fName, String fType, String f_gsm_min, String f_gsm_max, String w_min,
                             String fc_name, String fc_code, String date, String loca, String price,String phn_num) {

            tvFabricName.setText(fName);
            tvFabricType.setText(fType);
            tvFabricGsmMinMax.setText("GSM : " + f_gsm_min + " ~ " + f_gsm_max);
            tvWeightMinMax.setText("WEIGHT: " + w_min + " kg");
            tvFabricColor.setText("COLOR : " + fc_name);
            tvLocation.setText("LOCATION : " + loca);
            tvPrice.setText("PRICE : " + price + " tk/kg");
            tvDate.setText(date+"\n\n\n"+phn_num);

            cardView.setBackgroundColor(Color.parseColor(fc_code));
            tvFabricName.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvFabricType.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvFabricGsmMinMax.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvWeightMinMax.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvFabricColor.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvLocation.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvPrice.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvDate.setTextColor(Color.parseColor(invertColor(fc_code)));

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


    //for search
    public void filter(List<SellerPostItemList> searchName) {

        postList = new ArrayList<>();
        postList.addAll(searchName);
        notifyDataSetChanged();
    }
}
