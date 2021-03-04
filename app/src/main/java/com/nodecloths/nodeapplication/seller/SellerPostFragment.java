package com.nodecloths.nodeapplication.seller;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.activity.CreatPost;
import com.nodecloths.nodeapplication.adapter.SellerPostAdapter;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.model.SellerPostItemList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.nodecloths.nodeapplication.helper.Common.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerPostFragment extends Fragment {

    private View view;
    private RecyclerView rvAllPost;
    private TextView tvAllPost;
    private List<SellerPostItemList> sellerpostItemLists = new ArrayList<>();
    private SellerPostAdapter allPostAdapter;
    private static ProgressDialog progressDialog;
    private EditText etSearch;
    private Button buttonCreatePost;
    public SellerPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buyer_post, container, false);
        initComponents();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        rvAllPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        allPostAdapter = new SellerPostAdapter(getContext(), sellerpostItemLists);
        Common.setTextOnToolbar("Node Application");
        getPostItem();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchData(s.toString());

            }
        });

        buttonCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreatPost.class);
                intent.putExtra("Frame","buying");
                startActivity(intent);
                getActivity().finish();
                Common.selectFragment = "fabrics";
            }
        });
        return view;
    }

    private void initComponents() {
        rvAllPost = view.findViewById(R.id.rvAllPost);
        tvAllPost = view.findViewById(R.id.tvallPost);
        etSearch = view.findViewById(R.id.etSearch);
        buttonCreatePost = view.findViewById(R.id.buttonCreatePost);
        buttonCreatePost.setText("Create Post For Buying");
    }


    private void getPostItem() {
        progressDialog.show();
        AppController.getInstance().getRequestQueue().getCache().clear();
        sellerpostItemLists.clear();
        StringRequest request = new StringRequest(Request.Method.GET, Apis.sellerItemGet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object2 = new JSONObject(response);
                    JSONArray jsonArray = object2.getJSONArray("sellerpost");
                    Common.baseUrl = object2.getString("url");


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
                        String imgORcolor = object.getString("weight_max");
                        String f_colorName = object.getString("fabric_color");
                        String f_colorCode = object.getString("fabric_color_pantone");
                        String message = object.getString("additional_massage");
                        String loca = object.getString("location");
                        String price = object.getString("price");
                        String date = object.getString("date_and_time");
                        String visivility = "0";

                        sellerpostItemLists.add(new SellerPostItemList(id, phoneNum, status, fname, ftype, f_gsm_min, f_gsm_max,
                                w_min, imgORcolor, f_colorName, f_colorCode, loca, price, message, date,visivility));

                    }
                    Collections.reverse(sellerpostItemLists);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.toString());

                }

                if (sellerpostItemLists.size() > 0) {
                    rvAllPost.setAdapter(allPostAdapter);
                    allPostAdapter.notifyDataSetChanged();
                    tvAllPost.setVisibility(View.GONE);

                } else {
                    tvAllPost.setText("No Post Available");
                    rvAllPost.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
                tvAllPost.setText("Net Connection is very slow.");
                tvAllPost.setBackgroundColor(getResources().getColor(R.color.colorForth));
                rvAllPost.setVisibility(View.GONE);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    //----------For search-----------
    public void searchData(String text) {
        final List<SellerPostItemList> searchName = new ArrayList<>();
        searchName.clear();
        for (SellerPostItemList model : sellerpostItemLists) {
            if (model.getFabricType().toLowerCase().contains(text.toLowerCase()) ||
                    model.getFab_colorName().toLowerCase().contains(text.toLowerCase())
                    || model.getFabricName().toLowerCase().contains(text.toLowerCase())) {
                searchName.add(model);
            }
        }
        allPostAdapter.filter(searchName);
    }
    

}
