package com.example.inclass14;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnTripItemListener, OnPlaceItemListener {

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


        fab_add = findViewById(R.id.fab_add);

        ArrayList<Place> places1 = new ArrayList<>();
        places1.add(new Place("asdf", "http://",234.5, 42350));
        places1.add(new Place("asdf 2", "http://",234.5, 42350));
        data.add(new Trip("trip 1", "udhar", places1));

        ArrayList<Place> places3 = new ArrayList<>();
        places3.add(new Place("qwer", "http://",234.5, 42350));
        places3.add(new Place("qwer 2", "http://",234.5, 42350));
        places3.add(new Place("qwer 3", "http://",234.5, 42350));
        places3.add(new Place("qwer 4", "http://",234.5, 42350));
        data.add(new Trip("trip 3", "kidhar", places3));

        ArrayList<Place> places2 = new ArrayList<>();
        places2.add(new Place("zxcv", "http://",234.5, 42350));
        places2.add(new Place("zxcv 2", "http://",234.5, 42350));
        places2.add(new Place("zxcv 3", "http://",234.5, 42350));
        data.add(new Trip("trip 2", "idhar", places2));


        recycler_trip = findViewById(R.id.recycler_trip);
        recycler_trip.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycler_trip.setLayoutManager(layoutManager);
        mAdapter = new TripAdapter(data, this);
        recycler_trip.setAdapter(mAdapter);


        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(intent);

            }
        });


    }

    public void deletePlace(View view) {
        Log.d("asdf", "delete this: " + view.getTag());
    }

    public void showTripMap(View view) {
        Log.d("asdf", "trip map this: " + view.getTag());
    }

    public void addPlaceItem(View view) {
        Log.d("asdf", "add trip this: " + view.getTag());
    }

    @Override
    public void onItemClick(int adapterPosition) {
        Log.d("asdf", "onItemClick: " + adapterPosition);
    }
}
