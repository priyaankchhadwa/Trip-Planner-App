package com.example.inclass14;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddPlaceAdapter extends RecyclerView.Adapter<AddPlaceAdapter.ViewHolder> {


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_place_item, parent, false);
        return new AddPlaceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_add_place;
        ImageView iv_add_place;
        Button add_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_add_place = itemView.findViewById(R.id.tv_add_place);
            iv_add_place = itemView.findViewById(R.id.iv_add_place);
            add_button = itemView.findViewById(R.id.add_button);
        }
    }
}
