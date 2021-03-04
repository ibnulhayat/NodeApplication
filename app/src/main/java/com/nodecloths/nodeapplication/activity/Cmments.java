package com.nodecloths.nodeapplication.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.adapter.CommentsAdapter;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.model.CommentsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cmments extends AppCompatActivity {

    private ImageView ivBackSpace;
    private TextView tvToolbar, tvComment;
    private RecyclerView rvCommentList;
    private List<CommentsList> commentsLists = new ArrayList<>();
    private CommentsAdapter commentsAdapter;
    private static ProgressDialog progressDialog;
    private String post_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmments);
        initComponents();
        post_id = getIntent().getStringExtra("post_id");

        ivBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rvCommentList.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentsAdapter(Cmments.this, commentsLists);
        Common.setTextOnToolbar("My Post");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);


        getMyPost(post_id);
    }

    private void initComponents() {
        ivBackSpace = findViewById(R.id.ivBackSpace);
        tvToolbar = findViewById(R.id.tvToolbar);
        tvComment = findViewById(R.id.tvComment);
        rvCommentList = findViewById(R.id.rvCommentList);
        tvToolbar.setText("Comments");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void getMyPost(String post_id) {
        progressDialog.show();
        AppController.getInstance().getRequestQueue().getCache().clear();
        String url = Apis.comments_get + post_id;
        commentsLists.clear();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object2 = new JSONObject(response);
                    JSONArray jsonArray = object2.getJSONArray("comment");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String id = object.getString("id");
                        String post_id = object.getString("post_id");
                        String s_num = object.getString("seller_number");
                        String s_name = object.getString("seller_name");
                        String f_price = object.getString("fabric_price");
                        String s_location = object.getString("seller_location");

                        commentsLists.add(new CommentsList(id, post_id, s_num, s_name, f_price, s_location));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSONEXEPTION", e.toString());

                }

                if (commentsLists.size() > 0) {
                    rvCommentList.setAdapter(commentsAdapter);
                    commentsAdapter.notifyDataSetChanged();
                    tvComment.setVisibility(View.GONE);

                } else {
                    tvComment.setText("No Comments Available");
                    rvCommentList.setVisibility(View.GONE);

                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY", error.getMessage());

            }
        });

        AppController.getInstance().addToRequestQueue(request);

    }

}
