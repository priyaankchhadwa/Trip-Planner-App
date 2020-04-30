package com.example.inclass14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddPlaceActivity extends AppCompatActivity implements AddPlaceListener {

    private final OkHttpClient client = new OkHttpClient();

    private RecyclerView recycler_add_place;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore db;

    private ArrayList<Place> data = new ArrayList<>();
    private String trip_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        db = FirebaseFirestore.getInstance();

        recycler_add_place = findViewById(R.id.recycler_add_place);
        recycler_add_place.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycler_add_place.setLayoutManager(layoutManager);
        mAdapter = new AddPlaceAdapter(data, this);
        recycler_add_place.setAdapter(mAdapter);
        recycler_add_place.addItemDecoration(new DividerItemDecoration(recycler_add_place.getContext(),
                DividerItemDecoration.VERTICAL));

        Intent intent = getIntent();
        this.trip_ref = (String) intent.getSerializableExtra("trip_ref");
        Double lat = (Double) intent.getSerializableExtra("lat");
        Double lng = (Double) intent.getSerializableExtra("lng");


        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "key=" + getResources().getString(R.string.API_KEY_MAPS) + "&" +
                "location=" + lat + "," + lng + "&" +
                "radius=1000";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                parseJsonForNearBy(response.body().string());
//                Log.d("asdf", "onResponse: something is wrong?");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


    }

    @Override
    public void addPlaceToTrip(int pos) {
        db.collection("trips_test")
                .document(this.trip_ref)
                .collection("places")
                .document(data.get(pos).place_id)
                .set(data.get(pos).toHashMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("asdf", "onComplete: adding place to firestore successful");
                            finish();
                        } else {
                            Log.d("asdf", "onComplete: adding place to firestore failed");
                        }
                    }
                });
    }

    private void parseJsonForNearBy(String s) {
        try {
            JSONObject root = new JSONObject(s);
            String status = root.getString("status");

            JSONArray results = root.getJSONArray("results");

            for (int i = 1; i < results.length(); i++) {
                JSONObject obj = (JSONObject) results.get(i);

                Place p = new Place();

                JSONObject geometry = obj.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");

                p.latlng_place = new GeoPoint(location.getDouble("lat"), location.getDouble("lng"));
                p.place_id = obj.getString("place_id");
                p.img_url = obj.getString("icon");
                p.name = obj.getString("name");
                p.trip_id = this.trip_ref;
//                Log.d("asdf", p + "");
                data.add(p);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
