package com.nodecloths.nodeapplication.buyer;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.adapter.BuyerOwnPostAdapter;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.model.PostItemList;

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
    private RecyclerView rvMyPost;
    private TextView tvPost;
    private List<PostItemList> postItemLists = new ArrayList<>();
    private BuyerOwnPostAdapter buyerOwnPostAdapter;
    private static ProgressDialog progressDialog;

    public OwnPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buyer_own_post, container, false);
        initComponents();

        rvMyPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        buyerOwnPostAdapter = new BuyerOwnPostAdapter(getContext(), postItemLists);
        Common.setTextOnToolbar("My Post");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);


        getMyPost();
        return view;
    }


    private void initComponents() {
        rvMyPost = view.findViewById(R.id.rvMyPost);
        tvPost = view.findViewById(R.id.tvPost);

    }


    private void getMyPost(){
        progressDialog.show();
        String url = Apis.myItems+Common.phoneNum;
        postItemLists.clear();
        StringRequest request = new StringRequest(Request.Method.GET,url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object2 = new JSONObject(response);
                    JSONArray jsonArray = object2.getJSONArray("itempost");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String phoneNum = object.getString("mobile_number");

                            String id = object.getString("post_id");
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
                            String date = object.getString("date_and_time");
                            String comment = object.getString("comment");
                            String img = object.getString("image");
                            String postType = "own";

                            postItemLists.add(new PostItemList(id, phoneNum, status, fname, ftype, f_gsm_min, f_gsm_max,
                                    w_min, w_max, f_colorName, f_colorCode, message,date,comment,img,postType));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.toString());

                }

                if (postItemLists.size() > 0) {
                    rvMyPost.setAdapter(buyerOwnPostAdapter);
                    buyerOwnPostAdapter.notifyDataSetChanged();
                    tvPost.setVisibility(View.GONE);
                } else {
                    tvPost.setText("No Post Available");
                    rvMyPost.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                tvPost.setText("Net Connection is very slow.");
                rvMyPost.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        });
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(request);

    }

}
