package com.nodecloths.nodeapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.model.CommentsList;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Context mContext;
    private List<CommentsList> commentsList;

    public CommentsAdapter(Context mContext, List<CommentsList> List) {
        this.mContext = mContext;
        this.commentsList = List;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.comments_item_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder viewHolder, final int i) {
        CommentsList list = commentsList.get(i);
        final String id = list.getId();
        final String s_num = list.getSellerNumber();
        String s_name = list.getSellerName();
        String f_price = list.getFabricePrice();
        String s_location = list.getSellerLocation();

        viewHolder.setView(s_num, s_name, f_price, s_location);

        viewHolder.ivDialPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + s_num));
                mContext.startActivity(callIntent);
                Toast.makeText(mContext, "" + s_num, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSellerName, tvSellerLocation, tvSellerNumber,
                tvFabricPrice;
        private ImageView ivDialPad;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSellerName = itemView.findViewById(R.id.tvSellerName);
            tvSellerLocation = itemView.findViewById(R.id.tvSellerLocation);
            tvSellerNumber = itemView.findViewById(R.id.tvSellerNumber);
            tvFabricPrice = itemView.findViewById(R.id.tvFabricPrice);
            ivDialPad = itemView.findViewById(R.id.ivDialPad);
            cardView = itemView.findViewById(R.id.cardView);

        }

        public void setView(String s_num, String s_name, String f_price, String s_loca) {

            tvSellerName.setText("Name : " + s_name);
            tvSellerLocation.setText("Location : " + s_loca);
            tvSellerNumber.setText("Phone : " + s_num);
            tvFabricPrice.setText("Message : " + f_price);

        }

    }


}
