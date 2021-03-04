package com.nodecloths.nodeapplication.factory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.activity.CreatPost;
import com.nodecloths.nodeapplication.adapter.FactotyListAdapter;
import com.nodecloths.nodeapplication.adapter.SellerPostAdapter;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.model.FactoryList;
import com.nodecloths.nodeapplication.model.SellerPostItemList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.nodecloths.nodeapplication.helper.Common.TAG;


public class FactoryListFragment extends Fragment {
    private RecyclerView rvFactoryList;
    private TextView tvFactoryText;
    private static ProgressDialog progressDialog;
    private Spinner spFactorySearch;
    private Button buttonFactory;
    private List<FactoryList> factoryLists = new ArrayList<>();
    private FactotyListAdapter factotyListAdapter;

    public FactoryListFragment() {
        // Required empty public constructor
    }

    private String[] facType = {"Select Factory Type","Garments","Knitting","Knitting & Dyeing","Yarn manufacturing","Dyeing","Print","Embroidery","Accessories manufacturing","Sweater","Spinning","Coposit"};
    private String searchFac;
    private String search = "no";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_factory_list, container, false);

        initComponents(view);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        rvFactoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        factotyListAdapter = new FactotyListAdapter(getContext(), factoryLists);
        rvFactoryList.setAdapter(factotyListAdapter);
        getPostItem();

//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                searchData(s.toString());
//
//            }
//        });

        final List<String> plantsList = new ArrayList<>(Arrays.asList(facType));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.support_simple_spinner_dropdown_item, plantsList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spFactorySearch.setAdapter(spinnerArrayAdapter);
        spFactorySearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Factory Type")) {
                    // nothing do
                    if (!search.equals("no")){
                        searchFac = "";
                        searchData(searchFac);
                        Log.e(TAG,"hhhhh");
                    }

                } else {
                    searchFac= parent.getItemAtPosition(position).toString();
                    Log.e(TAG,searchFac);
                    searchData(searchFac.toLowerCase());
                    search = "yes";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreatPost.class);
                intent.putExtra("Frame","factory");
                startActivity(intent);
                getActivity().finish();
                Common.selectFragment = "factory";
            }
        });
        return view;
    }

    private void initComponents(View view) {
        rvFactoryList = view.findViewById(R.id.rvFactoryList);
        tvFactoryText = view.findViewById(R.id.tvFactoryText);
        spFactorySearch = view.findViewById(R.id.spFactorySearch);
        buttonFactory = view.findViewById(R.id.buttonFactory);
        buttonFactory.setText("Create a factory profile");
    }


    private void getPostItem() {
        progressDialog.show();
        AppController.getInstance().getRequestQueue().getCache().clear();
        factoryLists.clear();
        StringRequest request = new StringRequest(Request.Method.GET, Apis.factoryGet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object2 = new JSONObject(response);
                    JSONArray jsonArray = object2.getJSONArray("factory");
                    Common.baseUrl = object2.getString("url");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String status = object.getString("post_status");
                        if (status.equals("0")) {
                            String id = object.getString("id");
                            String phoneNum = object.getString("mobile_number");
                            String poster_name = object.getString("poster_name");
                            String factory_name = object.getString("factory_name");
                            String factory_type = object.getString("factory_type");
                            String specific = object.getString("specific");
                            String compliance = object.getString("compliance");
                            String production = object.getString("production");
                            String order = object.getString("order");
                            String location = object.getString("location");
                            String message = object.getString("description");
                            String picture = object.getString("picture");
                            String postType="";
                            factoryLists.add(new FactoryList(id, phoneNum, poster_name, factory_name, factory_type,
                                    specific, compliance, production, order, location, message, picture,postType));
                        }
                    }
                    Collections.reverse(factoryLists);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.toString());

                }

                if (factoryLists.size() > 0) {
                    factotyListAdapter.notifyDataSetChanged();
                    tvFactoryText.setVisibility(View.GONE);

                } else {
                    tvFactoryText.setText("No Post Available");
                    rvFactoryList.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                tvFactoryText.setText("Net Connection is very slow.");
                progressDialog.dismiss();
                tvFactoryText.setBackgroundColor(getResources().getColor(R.color.colorForth));
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    //----------For search-----------
    public void searchData(String text) {
        final List<FactoryList> searchName = new ArrayList<>();
        searchName.clear();
        for (FactoryList model : factoryLists) {
            if (model.getFactory_name().toLowerCase().contains(text.toLowerCase()) ||
                    model.getFactory_type().toLowerCase().contains(text.toLowerCase())) {
                searchName.add(model);
            }
        }
        factotyListAdapter.filter(searchName);
    }

}
