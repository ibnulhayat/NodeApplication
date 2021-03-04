package com.nodecloths.nodeapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.activity.Cmments;
import com.nodecloths.nodeapplication.activity.BuyerItemDetails;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.model.PostItemList;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class BuyerOwnPostAdapter extends RecyclerView.Adapter<BuyerOwnPostAdapter.ViewHolder> {

    private Context mContext;
    private List<PostItemList> postList;

    public BuyerOwnPostAdapter(Context mContext, List<PostItemList> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @NonNull
    @Override
    public BuyerOwnPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.buyer_post_item_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BuyerOwnPostAdapter.ViewHolder viewHolder, final int i) {
        PostItemList list = postList.get(i);
        final String p_id = list.getId();
        String phn_num = list.getPhoneNum();
        String status = list.getPostStatus();
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

        if (postType.equals("own")) {
            viewHolder.ivDelete.setImageResource(R.drawable.ic_delete);
            viewHolder.ivDelete.setColorFilter(Color.parseColor(invertColor(fc_code)));
            if (!comment.equals("0")) {
                viewHolder.bidButton.setText("Total Bid  " + comment);
            }else {
                viewHolder.bidButton.setText("Total Bid");
            }

        }

        viewHolder.setView(fName, fType, f_gsm_min, f_gsm_max, w_min, w_max, fc_name, fc_code, date,comment);

        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailog(p_id,i);

            }
        });

        viewHolder.bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Cmments.class);
                intent.putExtra("post_id", p_id);
                mContext.startActivity(intent);
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
                intent.putExtra("mypost", "1");

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

    private void itemDELETE(String p_id) {
        String url = Apis.itemDelete + p_id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext, "" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ITEMDELETE_ERROR", error.toString());
            }
        });
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(request);

    }

    private void commentDELETE(String p_id) {
        String url = Apis.commentsDelete + p_id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(mContext, "" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ITEMDELETE_ERROR", error.toString());
            }
        });
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(request);

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFabricName,tvFabricType, tvFabricGsmMinMax, tvWeightMinMax,
                tvFabricColor, tvDate;
        private Button bidButton;
        private ConstraintLayout etLayout;
        private ConstraintLayout layoutBackImage;
        private ImageView  ivDelete;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvFabricName = itemView.findViewById(R.id.tvFabricName);
            layoutBackImage = itemView.findViewById(R.id.layoutBackImage);
            tvFabricType = itemView.findViewById(R.id.tvFabricType);
            tvFabricGsmMinMax = itemView.findViewById(R.id.tvFabricGsmMinMax);
            tvWeightMinMax = itemView.findViewById(R.id.tvWeightMinMax);
            tvFabricColor = itemView.findViewById(R.id.tvFabricColor);
            bidButton = itemView.findViewById(R.id.callButton);
            etLayout = itemView.findViewById(R.id.etLayout);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            cardView = itemView.findViewById(R.id.cardView);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        public void setView(String fName, String fType, String f_gsm_min, String f_gsm_max, String w_min, String w_max, String fc_name, String fc_code, String date, String comment) {

            tvFabricName.setText(fName);
            tvFabricType.setText(fType);
            tvFabricGsmMinMax.setText("GSM : " + f_gsm_min + " ~ " + f_gsm_max);
            tvWeightMinMax.setText("WEIGHT: " + w_min + " ~ " + w_max + " kg");
            tvFabricColor.setText("COLOR : " + fc_name);
            tvDate.setText(date);

            cardView.setBackgroundColor(Color.parseColor(fc_code));
            tvFabricName.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvFabricType.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvFabricGsmMinMax.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvWeightMinMax.setTextColor(Color.parseColor(invertColor(fc_code)));
            tvFabricColor.setTextColor(Color.parseColor(invertColor(fc_code)));
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


    private void showDailog(final String p_id, final int i){
        new AlertDialog.Builder(mContext).setTitle("Confirm Delete?")
                .setMessage("Are you sure?")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                itemDELETE(p_id);
                                commentDELETE(p_id);
                                postList.remove(i);
                                notifyDataSetChanged();
                                // Perform Action & Dismiss dialog
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}
