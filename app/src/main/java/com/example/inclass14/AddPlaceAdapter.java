package com.example.inclass14;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddPlaceAdapter extends RecyclerView.Adapter<AddPlaceAdapter.ViewHolder> {

    private ArrayList<Place> mData;
    private AddPlaceListener maddPlaceListener;

    AddPlaceAdapter(ArrayList<Place> mData, AddPlaceListener addPlaceListener) {
        this.mData = mData;
        this.maddPlaceListener = addPlaceListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_place_item, parent, false);
        return new AddPlaceAdapter.ViewHolder(view, maddPlaceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = mData.get(position);

        holder.tv_add_place.setText(place.name);


        Picasso.get()
                .load(!place.img_url.equals("")? place.img_url: "http://")
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.iv_add_place);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_add_place;
        ImageView iv_add_place;
        ImageView btn_add;
        AddPlaceListener addPlaceListener;

        public ViewHolder(@NonNull View itemView, AddPlaceListener addPlaceListener) {
            super(itemView);
            tv_add_place = itemView.findViewById(R.id.tv_add_place);
            iv_add_place = itemView.findViewById(R.id.iv_add_place);
            btn_add = itemView.findViewById(R.id.iv_add_btn);
            this.addPlaceListener = addPlaceListener;

            btn_add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            addPlaceListener.addPlaceToTrip(getAdapterPosition());
        }
    }
}
