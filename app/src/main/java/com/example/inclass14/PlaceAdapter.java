package com.example.inclass14;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private ArrayList<Place> mData;
    private OnPlaceItemListener mOnPlaceItemListener;

    public PlaceAdapter(ArrayList<Place> mData, OnPlaceItemListener mOnPlaceItemListener) {
        this.mData = mData;
        this.mOnPlaceItemListener = mOnPlaceItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        return new PlaceAdapter.ViewHolder(view, mOnPlaceItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = mData.get(position);

        holder.tv_place_name.setText(place.name);
        holder.iv_delete.setTag(place);

        Picasso.get()
                .load(!place.img_url.equals("")? place.img_url: "http://")
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.iv_place_img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_place_name;
        ImageView iv_place_img, iv_delete;

        private OnPlaceItemListener onPlaceItemListener;

        public ViewHolder(@NonNull final View itemView, OnPlaceItemListener onPlaceItemListener) {
            super(itemView);
            tv_place_name = itemView.findViewById(R.id.tv_place_name);
            iv_place_img = itemView.findViewById(R.id.iv_place_img);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            this.onPlaceItemListener = onPlaceItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPlaceItemListener.onItemClick(getLayoutPosition());
        }

    }

}
