package com.example.inclass14;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.google.firebase.firestore.DocumentChange.Type.ADDED;

class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> implements PlaceAdapter.deletePlaceFromTrip {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<Trip> mData;
    private OnTripItemListener mOnTripItemListener;
    RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context trip_ctx;

    TripAdapter(ArrayList<Trip> mData, OnTripItemListener mOnTripItemListener, Context applicationContext) {
        this.mData = mData;
        this.mOnTripItemListener = mOnTripItemListener;
        this.trip_ctx = applicationContext;

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
        holder.recycler_places_adater = new PlaceAdapter(trip.place, this, position);
        holder.recycler_places.setLayoutManager(linearLayoutManager);
        holder.recycler_places.setAdapter(holder.recycler_places_adater);

        Log.d("asdf", "trip id for trip: " + trip + " and then id: " + trip.place);


        Log.d("asdf", "inside trip adapter to update inner recycler!!");
        db.collection("trips_test")
                .document(trip.id)
                .collection("places")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {


                        if (e != null) {
                            Log.d("", "Error : " + e.getMessage());
                        }

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            Place p;

                            switch (doc.getType()) {
                                case ADDED:
                                    Log.d("asdf", "adding place from inside trip adapter: " + doc.getDocument().getData());
                                    p = doc.getDocument().toObject(Place.class);
                                    if (valPlaces(trip.place, p)) {
                                        trip.place.add(p);
                                        holder.recycler_places_adater.notifyDataSetChanged();
                                    }
                                    break;
                                case MODIFIED:
                                    Log.d("asdf", "Modified contact: " + doc.getDocument().getData());
                                    holder.recycler_places_adater.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    Log.d("asdf", "Removed contact: " + doc.getDocument().getData());
                                    holder.recycler_places_adater.notifyDataSetChanged();
                                    break;
                            }
                        }
//                        holder.recycler_places_adater.notifyDataSetChanged();

                    }
                });

    }


    private boolean valPlaces(ArrayList<Place> places, Place p) {
        for (Place pi : places) {
            if (pi.place_id.equals(p.place_id)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void deletePlace(int parent_index, int place_position, String trip_id, String place_id) {
        db.collection("trips_test")
                .document(trip_id)
                .collection("places")
                .document(place_id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(trip_ctx, "Place deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(trip_ctx, "Place is not deleted", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        mData.get(parent_index).place.remove(place_position);
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
