package com.example.inclass14;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.google.firebase.firestore.DocumentChange.Type.ADDED;

class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Trip trip = mData.get(position);

        holder.tv_trip_title.setText(trip.trip_title);
        holder.tv_location.setText(trip.location);

        holder.iv_map.setTag(trip);
        holder.iv_add_to_trip.setTag(trip);

        holder.recycler_places.setRecycledViewPool(viewPool);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.itemView.getContext());

        holder.recycler_places.addItemDecoration(new DividerItemDecoration(holder.recycler_places.getContext(),
                linearLayoutManager.getOrientation()));
        holder.recycler_places_adater = new PlaceAdapter(trip.place);
        holder.recycler_places.setLayoutManager(linearLayoutManager);
        holder.recycler_places.setAdapter(holder.recycler_places_adater);

        Log.d("asdf", "trip id for trip: " + trip + " and then id: " + trip.id);


//        db.collection("trips_test")
//                .document(trip.id)
//                .collection("places")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.d("", "Error : " + e.getMessage());
//                        }
//
//                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
//                            Place p;
//                            if (doc.getType() == ADDED) {
//                                Log.d("asdf","adding place"+ doc.getDocument().getId());
//                                p = doc.getDocument().toObject(Place.class);
//                                trip.place.add(p);
//                                Log.d("asdf", "ajhsdvjasvd" + p);
//                            }
//                        }
//
//                    }
//                });
//
//        holder.recycler_places_adater.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_trip_title, tv_location;
        ImageView iv_add_to_trip, iv_map;
        RecyclerView recycler_places;
        RecyclerView.Adapter recycler_places_adater;
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
            onTripItemListener.onTripItemClick(getLayoutPosition());
        }

    }
}
