package com.nodecloths.nodeapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.activity.SellerItemDetails;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.SqliteDB;
import com.nodecloths.nodeapplication.model.FactoryList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.nodecloths.nodeapplication.helper.Common.TAG;
import static com.nodecloths.nodeapplication.helper.Common.baseUrl;

public class FactotyListAdapter extends RecyclerView.Adapter<FactotyListAdapter.ViewHolder> {

    private Context mContext;
    private List<FactoryList> postList;
    RequestQueue mRequestQueue;
    private SqliteDB sqliteDB;

    public FactotyListAdapter(Context mContext, List<FactoryList> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @NonNull
    @Override
    public FactotyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        sqliteDB = new SqliteDB(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.factory_list_item_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FactotyListAdapter.ViewHolder viewHolder, final int i) {


        final FactoryList list = postList.get(i);
        final String p_id = list.getId();
        final String phn_num = list.getMobile_number();
        final String facName = list.getFactory_name();
        final String facType = list.getFactory_type();
        final String specific = list.getSpecific();
        final String compliance = list.getCompliance();
        final String production = list.getProduction();
        final String order = list.getOrder();
        final String location = list.getLocation();
        final String message = list.getDescription();
        final String picture = list.getPicture();
        String postType = list.getPostType();

        viewHolder.setView(facName,facType,production,location);


        if (picture.equals("null")){
            viewHolder.ivFactoryImage.setImageResource(R.drawable.noimage);
        }else {
            Picasso.get().load(baseUrl+picture).placeholder(R.drawable.loading).into(viewHolder.ivFactoryImage);
        }

        if (postType.equals("own")){
            viewHolder.ivCall.setImageResource(R.drawable.ic_delete);
            viewHolder.ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDailog(p_id, i);


                }
            });
        }else {
            viewHolder.ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phn_num));
                    mContext.startActivity(callIntent);
                    Toast.makeText(mContext, "" + phn_num, Toast.LENGTH_SHORT).show();

                }
            });
        }



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SellerItemDetails.class);
                intent.putExtra("type","factory");
                intent.putExtra("P_ID", p_id);
                intent.putExtra("phn_num", phn_num);
                intent.putExtra("facName", facName);
                intent.putExtra("facType", facType);
                intent.putExtra("specific", specific);
                intent.putExtra("compliance", compliance);
                intent.putExtra("production", production);
                intent.putExtra("order", order);
                intent.putExtra("location", location);
                intent.putExtra("picture", picture);
                intent.putExtra("message", message);
                intent.putExtra("mypost", list.getPostType());
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFactoryName, tvFactoryType, tvProduction, tvLocation;

        private ImageView ivFactoryImage, ivCall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvFactoryName = itemView.findViewById(R.id.tvSetFactoryName);
            tvFactoryType = itemView.findViewById(R.id.tvSetFactoryType);
            tvProduction = itemView.findViewById(R.id.tvSetProduction);
            tvLocation = itemView.findViewById(R.id.tvSetFactoryLocation);
            ivFactoryImage = itemView.findViewById(R.id.ivSetFactoryImage);
            ivCall = itemView.findViewById(R.id.ivCall);
        }
        public void setView(String fName, String fType, String production, String loca) {
            tvFactoryName.setText(fName);
            tvFactoryType.setText(fType);
            tvProduction.setText(production+" Pieces/Day");
            tvLocation.setText("Location : " + loca);
        }
    }

    //for search
    public void filter(List<FactoryList> searchName) {

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
        String url = Apis.factoryItemDelete + p_id;
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

}
