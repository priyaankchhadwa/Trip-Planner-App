package com.example.inclass14;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnTripItemListener {

    private FloatingActionButton fab_add;

    private RecyclerView recycler_trip;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    ArrayList<Trip> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        fab_add = findViewById(R.id.fab_add);

//        ArrayList<Place> places1 = new ArrayList<>();
//        places1.add(new Place("asdf", "https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png",234.5, 42350));
//        places1.add(new Place("asdf 2", "http://",234.5, 42350));
//        data.add(new Trip("trip 1", "udhar", places1));
//
//        ArrayList<Place> places3 = new ArrayList<>();
//        places3.add(new Place("qwer", "http://",234.5, 42350));
//        places3.add(new Place("qwer 2", "http://",234.5, 42350));
//        places3.add(new Place("qwer 3", "http://",234.5, 42350));
//        places3.add(new Place("qwer 4", "http://",234.5, 42350));
//        data.add(new Trip("trip 3", "kidhar", places3));
//
//        ArrayList<Place> places2 = new ArrayList<>();
//        places2.add(new Place("zxcv", "http://",234.5, 42350));
//        places2.add(new Place("zxcv 2", "http://",234.5, 42350));
//        places2.add(new Place("zxcv 3", "http://",234.5, 42350));
//        data.add(new Trip("trip 2", "idhar", places2));

        recycler_trip = findViewById(R.id.recycler_trip);
        recycler_trip.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycler_trip.setLayoutManager(layoutManager);
        mAdapter = new TripAdapter(data, this, getApplicationContext());
        recycler_trip.setAdapter(mAdapter);


        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(intent);

            }
        });

        db.collection("trips_test")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("asdf", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d("asdf", "New contact: " + dc.getDocument().getData());

                                    HashMap document = (HashMap) dc.getDocument().getData();

                                    final Trip trip = new Trip();
                                    trip.id = dc.getDocument().getId();
                                    trip.trip_title = (String) document.get("trip_title");
                                    trip.location = (String) document.get("location");
                                    trip.latlng_city = (GeoPoint) document.get("latlng_city");
                                    trip.place_id = (String) document.get("place_id");

                                    data.add(trip);
                                    mAdapter.notifyItemInserted(data.size() - 1);

                                    break;
                                case MODIFIED:
                                    Log.d("asdf", "Modified contact: " + dc.getDocument().getData());
                                    mAdapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    Log.d("asdf", "Removed contact: " + dc.getDocument().getData());
                                    mAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    public void deletePlace(View view) {
        Log.d("asdf", "delete this obj: " + view.getTag());
        Place place = (Place) view.getTag();
        db.collection("trips_test")
                .document(place.trip_id)
                .collection("places")
                .document(place.place_id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Place deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Place is not deleted", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

//        trip.get(parent_index).places.remove(pos);
    }

    public void showTripMap(View view) {
        Log.d("asdf", "trip map this: " + view.getTag());
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("trip", (Trip) view.getTag());
        i.putParcelableArrayListExtra("places", ((Trip) view.getTag()).place);
        startActivity(i);

    }

    public void addPlaceItem(View view) {
        Log.d("asdf", "add trip this: " + view.getTag());
        Trip trip = (Trip) view.getTag();
        Intent intent = new Intent(MainActivity.this, AddPlaceActivity.class);
        intent.putExtra("trip_ref",trip.id);
        intent.putExtra("lat", trip.latlng_city.getLatitude());
        intent.putExtra("lng", trip.latlng_city.getLongitude());
        startActivity(intent);
    }

    @Override
    public void onTripItemClick(int adapterPosition) {
        Log.d("asdf", "onItemClick: " + adapterPosition);

    }
}
