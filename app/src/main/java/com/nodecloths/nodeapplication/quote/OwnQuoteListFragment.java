
package com.nodecloths.nodeapplication.quote;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.nodecloths.nodeapplication.adapter.QuoteListAdapter;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.model.QuoteList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.nodecloths.nodeapplication.helper.Apis.myQuoteList;
import static com.nodecloths.nodeapplication.helper.Common.TAG;


public class OwnQuoteListFragment extends Fragment {
    private RecyclerView rvQuoteList;
    private TextView tvQuoteText;
    private static ProgressDialog progressDialog;
    private List<QuoteList> quoteLists = new ArrayList<>();
    private QuoteListAdapter quoteListAdapter;

    public OwnQuoteListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_own_quote_list, container, false);
        initComponents(view);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        rvQuoteList.setLayoutManager(new LinearLayoutManager(getActivity()));
        quoteListAdapter = new QuoteListAdapter(getContext(), quoteLists);
        rvQuoteList.setAdapter(quoteListAdapter);
        getPostItem();

        return view;
    }

    private void initComponents(View view) {
        rvQuoteList = view.findViewById(R.id.rvQuoteList);
        tvQuoteText = view.findViewById(R.id.tvQuoteText);
    }


    private void getPostItem() {
        progressDialog.show();
        AppController.getInstance().getRequestQueue().getCache().clear();
        quoteLists.clear();
        StringRequest request = new StringRequest(Request.Method.GET, myQuoteList+Common.phoneNum, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object2 = new JSONObject(response);
                    JSONArray jsonArray = object2.getJSONArray("quote");
                    Common.baseUrl = object2.getString("url");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String phoneNum = object.getString("mobile_number");
                        String item_name = object.getString("item_name");
                        String fab_name = object.getString("fab_name");
                        String fab_type = object.getString("fab_type");
                        String gsm = object.getString("gsm");
                        String design = object.getString("design");
                        String quantity = object.getString("quantity");
                        String size = object.getString("size");
                        String measurement_type = object.getString("measurement_type");
                        String message = object.getString("description");
                        String picture1 = object.getString("img_1");
                        String picture2 = object.getString("img_2");
                        String picture3 = object.getString("img_3");
                        String picture4 = object.getString("img_4");
                        String postType="own";
                        List<String> imgList = new ArrayList<>();
                        if (!picture1.contains("null")) {
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
                        quoteLists.add(new QuoteList(id, phoneNum, item_name, fab_name, fab_type,
                                gsm, design, quantity, size, measurement_type, message, picture1,
                                imgList,postType));

                    }
                    Collections.reverse(quoteLists);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.toString());

                }

                if (quoteLists.size() > 0) {
                    quoteListAdapter.notifyDataSetChanged();
                    tvQuoteText.setVisibility(View.GONE);

                } else {
                    tvQuoteText.setText("No Post Available");
                    rvQuoteList.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                tvQuoteText.setText("Net Connection is very slow.");
                progressDialog.dismiss();
                tvQuoteText.setBackgroundColor(getResources().getColor(R.color.colorForth));
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

}
