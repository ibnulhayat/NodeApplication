package com.nodecloths.nodeapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.model.ColorsModel;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ColorsModel> colorList;

    private RecyclerViewClickLister mListener;

    public ColorAdapter(Context mContext, ArrayList<ColorsModel> colorList, RecyclerViewClickLister listener) {
        this.mContext = mContext;
        this.colorList = colorList;
        this.mListener = listener;
    }

    public interface RecyclerViewClickLister {
        void onClick(View view, int position, ArrayList<ColorsModel> list);
    }

    @NonNull
    @Override
    public ColorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_color_picker, viewGroup, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.ViewHolder viewHolder, int i) {
        String colorName = colorList.get(i).getColorName();
        String haxCode = colorList.get(i).getHexColorCode();
        viewHolder.tvColorName.setText(colorName);
        viewHolder.cardView.setCardBackgroundColor(Color.parseColor(haxCode));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvColorName;
        CardView cardView;
        private RecyclerViewClickLister mListener;

        public ViewHolder(@NonNull View itemView, RecyclerViewClickLister listener) {
            super(itemView);
            this.mListener = listener;
            itemView.setOnClickListener(this);

            tvColorName = itemView.findViewById(R.id.tvColorName);
            cardView = itemView.findViewById(R.id.cardView);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(v, getAdapterPosition(), colorList);
            }
        }
    }

    //for search
    public void filter(List<ColorsModel> searchName) {

        colorList = new ArrayList<>();
        colorList.addAll(searchName);
        notifyDataSetChanged();
    }
}
