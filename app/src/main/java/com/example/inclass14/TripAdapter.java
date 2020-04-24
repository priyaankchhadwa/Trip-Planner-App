package com.example.inclass14;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> implements OnPlaceItemListener {

    private ArrayList<Trip> mData;
    private OnTripItemListener mOnTripItemListener;
    RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();


    TripAdapter(ArrayList<Trip> mData, OnTripItemListener mOnTripItemListener) {
        this.mData = mData;
        this.mOnTripItemListener = mOnTripItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        return new ViewHolder(view, mOnTripItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = mData.get(position);

        holder.tv_trip_title.setText(trip.trip_title);
        holder.tv_location.setText(trip.Location);

        holder.iv_map.setTag(trip);
        holder.iv_add_to_trip.setTag(trip);

        holder.recycler_places.setRecycledViewPool(viewPool);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.itemView.getContext());

        holder.recycler_places.setLayoutManager(linearLayoutManager);
        holder.recycler_places.setAdapter(new PlaceAdapter(trip.place, this));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onItemClick(int adapterPosition) {
        Log.d("asdf", "in trip adapter and clicked: " + adapterPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_trip_title, tv_location;
        ImageView iv_add_to_trip, iv_map;
        RecyclerView recycler_places;
        private OnTripItemListener onTripItemListener;

        public ViewHolder(@NonNull final View itemView, OnTripItemListener onTripItemListener) {
            super(itemView);
            tv_trip_title = itemView.findViewById(R.id.tv_trip_title);
            tv_location = itemView.findViewById(R.id.tv_location);
            iv_add_to_trip = itemView.findViewById(R.id.iv_add_to_trip);
            iv_map = itemView.findViewById(R.id.iv_map);
            recycler_places = itemView.findViewById(R.id.recycler_places);
            this.onTripItemListener = onTripItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTripItemListener.onItemClick(getLayoutPosition());
        }

    }
}
