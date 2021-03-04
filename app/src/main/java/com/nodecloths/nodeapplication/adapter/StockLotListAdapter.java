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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.activity.DetailsActivity;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.SqliteDB;
import com.nodecloths.nodeapplication.model.QuoteList;
import com.nodecloths.nodeapplication.model.StockLotList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.nodecloths.nodeapplication.helper.Common.TAG;
import static com.nodecloths.nodeapplication.helper.Common.baseUrl;

public class StockLotListAdapter extends RecyclerView.Adapter<StockLotListAdapter.ViewHolder> {

    private Context mContext;
    private List<StockLotList> postList;
    RequestQueue mRequestQueue;
    private SqliteDB sqliteDB;

    public StockLotListAdapter(Context mContext, List<StockLotList> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @NonNull
    @Override
    public StockLotListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        sqliteDB = new SqliteDB(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.stock_lot_item_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StockLotListAdapter.ViewHolder viewHolder, final int i) {


        final StockLotList list = postList.get(i);
        final String p_id = list.getId();
        final String phn_num = list.getMobile_number();
        final String item_name = list.getTitle();
        final String brand = list.getBrand();
        final String fab_type = list.getFabrics();
        final String quantity = list.getQuantity();
        final String price = list.getPrice();
        final String location = list.getLocation();
        final String size = list.getSize();
        final String img = list.getBig_img();
        final String message = list.getDescription();
        final String timeDate = list.getTimeDate();
        final String postType = list.getPostType();
        final List<String> imgList = list.getList();
        final String[] arr = timeDate.split("/");
        final String date = arr[0];
        final String time = arr[1];


        viewHolder.setView(item_name,brand,quantity,location,date,time);

        if (img.contains("null")){
            viewHolder.ivStockImageView.setImageResource(R.drawable.noimage);
        }else {
            Picasso.get().load(baseUrl+img).placeholder(R.drawable.loading).into(viewHolder.ivStockImageView);
        }


        if (postType.equals("own")){
            viewHolder.ivDial.setImageResource(R.drawable.ic_delete);
            viewHolder.ivDial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDailog(p_id, i);
                }
            });
        }else {
            viewHolder.ivDial.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("type", "stockLot");
                intent.putExtra("p_ID", p_id);
                intent.putExtra("phn_num", phn_num);
                intent.putExtra("title", item_name);
                intent.putExtra("brand", brand);
                intent.putExtra("fabType", fab_type);
                intent.putExtra("quantity", quantity);
                intent.putExtra("price", price);
                intent.putExtra("location", location);
                intent.putExtra("date", date);
                intent.putExtra("size", size);
                intent.putExtra("message", message);
                intent.putStringArrayListExtra("list",(ArrayList<String>) imgList);
                intent.putExtra("postType", list.getPostType());
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSetStockText;

        private ImageView ivDial, ivStockImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSetStockText = itemView.findViewById(R.id.tvSetStockText);
            ivDial = itemView.findViewById(R.id.ivDial);
            ivStockImageView = itemView.findViewById(R.id.ivStockImageView);
        }

        public void setView(String item_name, String brand, String quantity, String loca, String date,String time) {

            tvSetStockText.setText(item_name+"\n"+brand+"\n"+quantity+" Pieces"+"\n"+loca+"\n"+date+"\n"+time);

        }
    }

    //for search
    public void filter(List<StockLotList> searchName) {

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
        String url = Apis.stockLotItemDelete + p_id;
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
