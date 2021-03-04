package com.nodecloths.nodeapplication.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
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
import com.nodecloths.nodeapplication.activity.Cmments;
import com.nodecloths.nodeapplication.activity.DetailsActivity;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.helper.SqliteDB;
import com.nodecloths.nodeapplication.model.ApplyRecordLIst;
import com.nodecloths.nodeapplication.model.FactoryList;
import com.nodecloths.nodeapplication.model.QuoteList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nodecloths.nodeapplication.helper.Common.TAG;
import static com.nodecloths.nodeapplication.helper.Common.baseUrl;

public class QuoteListAdapter extends RecyclerView.Adapter<QuoteListAdapter.ViewHolder> {

    private Context mContext;
    private List<QuoteList> postList;
    RequestQueue mRequestQueue;
    private SqliteDB sqliteDB;
    private List<ApplyRecordLIst> applyList;
    public QuoteListAdapter(Context mContext, List<QuoteList> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @NonNull
    @Override
    public QuoteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        sqliteDB = new SqliteDB(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.quote_list_item_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuoteListAdapter.ViewHolder viewHolder, final int i) {

        applyList = sqliteDB.applyList();

        final QuoteList list = postList.get(i);
        final String id = list.getId();
        final String p_id = "21"+id;
        final String phn_num = list.getMobile_number();
        final String item_name = list.getItem_name();
        final String fab_name = list.getFab_name();
        final String fab_type = list.getFab_type();
        final String gsm = list.getGsm();
        final String design = list.getDesign();
        final String quantity = list.getQuantity();
        final String size = list.getSize();
        final String measurement_type = list.getMeasurement_type();
        final String message = list.getDescription();
        final String img_1 = list.getImg_1();
        final String postType = list.getPostType();
        final List<String> imgList = list.getList();

        viewHolder.setView(item_name,quantity,fab_type,measurement_type);


        if (img_1.contains("null")){
            viewHolder.ivQuoteImageView.setImageResource(R.drawable.noimage);
        }else {
            Picasso.get().load(baseUrl+img_1).placeholder(R.drawable.loading).into(viewHolder.ivQuoteImageView);
        }


        if (postType.equals("own")){
            viewHolder.ivDial.setImageResource(R.drawable.ic_delete);
            viewHolder.ivDial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDailog(id, i);
                }
            });

            viewHolder.ivQuoteChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Cmments.class);
                    intent.putExtra("post_id", p_id);
                    mContext.startActivity(intent);
                }
            });

        }else {
            viewHolder.ivDial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.ivQuoteChat.setVisibility(View.VISIBLE);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phn_num));
                    mContext.startActivity(callIntent);
                    Toast.makeText(mContext, "" + phn_num, Toast.LENGTH_SHORT).show();
                }
            });

            String bitId = "";
            for (int j = 0; j < applyList.size(); j++) {
                bitId = applyList.get(j).getApplyPost_id();
                if (bitId.equals(p_id)) {
                    viewHolder.ivQuoteChat.setColorFilter(Color.parseColor("#717171"));
                }else {
                    viewHolder.ivQuoteChat.setColorFilter(Color.parseColor("#FFFFFF"));
                }
            }

            viewHolder.ivQuoteChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getStatus(p_id).equals("1")) {
                        Toast.makeText(mContext, "You already comment this post.", Toast.LENGTH_SHORT).show();
                    } else {
                        showDialog(p_id);
                    }
                }
            });
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("type", "quote");
                intent.putExtra("ID", list.getId());
                intent.putExtra("phn_num", phn_num);
                intent.putExtra("item_name", item_name);
                intent.putExtra("fab_name", fab_name);
                intent.putExtra("fab_type", fab_type);
                intent.putExtra("gsm", gsm);
                intent.putExtra("design", design);
                intent.putExtra("quantity", quantity);
                intent.putExtra("size", size);
                intent.putExtra("measurement_typ", measurement_type);
                intent.putExtra("message", message);
                intent.putStringArrayListExtra("QuoteList",(ArrayList<String>) imgList);
                intent.putExtra("postType", list.getPostType());
                mContext.startActivity(intent);
            }
        });


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
        private TextView tvSetQuoteText;

        private ImageView ivDial, ivQuoteImageView,ivQuoteChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSetQuoteText = itemView.findViewById(R.id.tvSetQuoteText);
            ivDial = itemView.findViewById(R.id.ivDial);
            ivQuoteImageView = itemView.findViewById(R.id.ivQuoteImageView);
            ivQuoteChat = itemView.findViewById(R.id.ivQuoteChat);
        }

        public void setView(String itemName, String quantity, String fabType, String mesur) {

            tvSetQuoteText.setText(itemName+"\n"+quantity+" Pieces"+"\n"+fabType+"\n"+mesur);

        }
    }

    //for search
    public void filter(List<QuoteList> searchName) {

        postList = new ArrayList<>();
        postList.addAll(searchName);
        notifyDataSetChanged();
    }


    private void showDailog(final String p_id, final int i) {
        new AlertDialog.Builder(mContext).setTitle("Confirm Delete?")
                .setMessage("Are you sure?")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                itemDELETE(p_id);
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

    private void itemDELETE(String p_id) {
        String url = Apis.quoteItemDelete + p_id;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext, "" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(request);

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
        etMessage.setHint("Message");
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
                    Toast.makeText(mContext, "Data Add", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(mContext, "" + response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError", error.toString());
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


}
