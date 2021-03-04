package com.nodecloths.nodeapplication.stock_lot;


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
import com.nodecloths.nodeapplication.adapter.BuyerPostAdapter;
import com.nodecloths.nodeapplication.adapter.QuoteListAdapter;
import com.nodecloths.nodeapplication.adapter.StockLotListAdapter;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.model.PostItemList;
import com.nodecloths.nodeapplication.model.QuoteList;
import com.nodecloths.nodeapplication.model.StockLotList;

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
public class StockLotListFragment extends Fragment {

    private RecyclerView rvStockLotList;
    private TextView tvStockLotText;
    private static ProgressDialog progressDialog;
    private EditText etSearch;
    private Button buttonStockLot;
    private List<StockLotList> stockLotLists = new ArrayList<>();
    private StockLotListAdapter stockLotListAdapter;

    public StockLotListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_lot_list, container, false);
        initComponents(view);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        rvStockLotList.setLayoutManager(new LinearLayoutManager(getActivity()));
        stockLotListAdapter = new StockLotListAdapter(getContext(), stockLotLists);
        rvStockLotList.setAdapter(stockLotListAdapter);
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



        buttonStockLot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreatPost.class);
                intent.putExtra("Frame","stockLot");
                startActivity(intent);
                getActivity().finish();
                Common.selectFragment = "stockLot";
            }
        });
        return view;
    }

    private void initComponents(View view) {
        rvStockLotList = view.findViewById(R.id.rvStockLotList);
        tvStockLotText = view.findViewById(R.id.tvStockLotText);
        etSearch = view.findViewById(R.id.etSearch);
        buttonStockLot = view.findViewById(R.id.buttonStockLot);
        buttonStockLot.setText("Sell an stock lot Items");
    }


    private void getPostItem() {
        progressDialog.show();
        AppController.getInstance().getRequestQueue().getCache().clear();
        stockLotLists.clear();
        StringRequest request = new StringRequest(Request.Method.GET, Apis.stokLotGet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object2 = new JSONObject(response);
                    JSONArray jsonArray = object2.getJSONArray("stockLot");
                    Common.baseUrl = object2.getString("url");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String phoneNum = object.getString("mobile_number");
                        String title = object.getString("title");
                        String brand = object.getString("brand");
                        String size = object.getString("size");
                        String fabrics = object.getString("fabrics");
                        String quantity = object.getString("quantity");
                        String price = object.getString("price");
                        String location = object.getString("location");
                        String message = object.getString("description");
                        String timeDate = object.getString("timeDate");
                        String bigImage = object.getString("big_img");
                        String picture1 = object.getString("img_1");
                        String picture2 = object.getString("img_2");
                        String picture3 = object.getString("img_3");
                        String picture4 = object.getString("img_4");
                        String picture5 = object.getString("img_5");
                        String picture6 = object.getString("img_6");
                        String picture7 = object.getString("img_7");
                        String picture8 = object.getString("img_8");
                        String postType="";
                        List<String> imgList = new ArrayList<>();
                        if (!bigImage.contains("null")) {
                            imgList.add(bigImage);
                        }if (!picture1.contains("null")) {
                            imgList.add(picture1);
                        }
                        if (!picture2.contains("null")) {
                            imgList.add(picture2);
                        }
                        if (!picture3.contains("null")) {
                            imgList.add(picture3);
                        }
                        if (!picture4.contains("null")) {
                            imgList.add(picture4);
                        }
                        if (!picture5.contains("null")) {
                            imgList.add(picture5);
                        }
                        if (!picture6.contains("null")) {
                            imgList.add(picture6);
                        }
                        if (!picture7.contains("null")) {
                            imgList.add(picture7);
                        }
                        if (!picture8.contains("null")) {
                            imgList.add(picture8);
                        }
                        stockLotLists.add(new StockLotList(id, phoneNum, title, brand, size,
                                fabrics, quantity,price ,location , message,timeDate,bigImage,
                                imgList,postType));

                    }
                    Collections.reverse(stockLotLists);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.toString());

                }

                if (stockLotLists.size() > 0) {
                    stockLotListAdapter.notifyDataSetChanged();
                    tvStockLotText.setVisibility(View.GONE);

                } else {
                    tvStockLotText.setText("No Post Available");
                    rvStockLotList.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                tvStockLotText.setText("Net Connection is very slow.");
                progressDialog.dismiss();
                tvStockLotText.setBackgroundColor(getResources().getColor(R.color.colorForth));
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    //----------For search-----------
    public void searchData(String text) {
        final List<StockLotList> searchName = new ArrayList<>();
        searchName.clear();
        for (StockLotList model : stockLotLists) {
            if (model.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    model.getBrand().toLowerCase().contains(text.toLowerCase())) {
                searchName.add(model);
            }
        }
        stockLotListAdapter.filter(searchName);
    }


}
