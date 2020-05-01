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
    TripAdapter ctx;
    int trip_idx;

    public PlaceAdapter(ArrayList<Place> mData, TripAdapter tripAdapter, int trip_idx) {
        this.mData = mData;
        this.ctx = tripAdapter;
        this.trip_idx = trip_idx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        return new PlaceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Place place = mData.get(position);

        holder.tv_place_name.setText(place.name);

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.deletePlace(trip_idx, position, place.trip_id, place.place_id);
            }
        });

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


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_place_name;
        ImageView iv_place_img, iv_delete;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tv_place_name = itemView.findViewById(R.id.tv_place_name);
            iv_place_img = itemView.findViewById(R.id.iv_place_img);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }

    }
    interface deletePlaceFromTrip{
        void deletePlace(int parent_index,int place_position, String trip_id, String place_id);
    }
}
