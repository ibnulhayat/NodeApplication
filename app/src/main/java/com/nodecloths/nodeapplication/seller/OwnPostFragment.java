package com.nodecloths.nodeapplication.seller;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.adapter.SellerOwnPostAdapter;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.model.SellerPostItemList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.nodecloths.nodeapplication.helper.Common.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnPostFragment extends Fragment {

    private View view;
    private RecyclerView rvSellerPost;
    private TextView tvPost;
    private List<SellerPostItemList> sellerpostItemLists;
    private SellerOwnPostAdapter postItemAdapter;
    private static ProgressDialog progressDialog;

    public OwnPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_seller_own_post, container, false);
        initComponents();
        sellerpostItemLists = new ArrayList<>();

        rvSellerPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        postItemAdapter = new SellerOwnPostAdapter(getContext(), sellerpostItemLists);
        Common.setTextOnToolbar("My Post");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);


        getMyPost2();
        return view;
    }


    private void initComponents() {
        rvSellerPost = view.findViewById(R.id.rvSellerPost);
        tvPost = view.findViewById(R.id.tvPost);

    }


    private void getMyPost2() {
        progressDialog.show();
        String url = Apis.sellerOwnItemGet + Common.phoneNum;
        sellerpostItemLists.clear();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object2 = new JSONObject(response);
                    JSONArray jsonArray = object2.getJSONArray("sellerpost");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String phoneNum = object.getString("mobile_number");
                        String id = object.getString("id");

                        String status = object.getString("post_status");
                        String fname = object.getString("fabric_name");
                        String ftype = object.getString("fabric_type");
                        String f_gsm_min = object.getString("fabric_gsm_min");
                        String f_gsm_max = object.getString("fabric_gsm_max");
                        String w_min = object.getString("weight_min");
                        String w_max = object.getString("weight_max");
                        String f_colorName = object.getString("fabric_color");
                        String f_colorCode = object.getString("fabric_color_pantone");
                        String message = object.getString("additional_massage");
                        String loca = object.getString("location");
                        String price = object.getString("price");
                        String date = object.getString("date_and_time");
                        String visivility = "1";

                        sellerpostItemLists.add(new SellerPostItemList(id, phoneNum, status, fname, ftype, f_gsm_min, f_gsm_max,
                                w_min, w_max, f_colorName, f_colorCode, loca, price, message, date,visivility));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                     Log.d(TAG, e.toString());

                }

                if (sellerpostItemLists.size() > 0) {
                    rvSellerPost.setAdapter(postItemAdapter);
                    postItemAdapter.notifyDataSetChanged();
                    tvPost.setVisibility(View.GONE);
                } else {
                    tvPost.setText("No Post Available");
                    rvSellerPost.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d(TAG, error.toString());
                tvPost.setText("Net Connection is very slow.");
                rvSellerPost.setVisibility(View.GONE);

            }
        });
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(request);

    }

}
