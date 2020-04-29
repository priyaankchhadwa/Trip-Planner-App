package com.example.inclass14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddTripActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private TextView tv_trip_name;
    private TextView tv_city_search;

    private Button btn_search;
    private Button btn_add_trip;

    double lng, lat;
    int selectedCity = -1;

    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> placeIds = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayAdapter<String> adapter;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setTitle("Add Trip");

        tv_trip_name = findViewById(R.id.tv_trip_name);
        tv_city_search = findViewById(R.id.tv_city_search);
        btn_search = findViewById(R.id.btn_search);
        btn_add_trip = findViewById(R.id.btn_add_trip);
        lv = findViewById(R.id.lv_search_city);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, data);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_city_search.setText(data.get(position));
                selectedCity = position;
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city_search = tv_city_search.getText().toString().trim();

                if (city_search.equals("")) {
                    tv_city_search.setError("Enter a city");
                } else {
                    data.clear();
                    placeIds.clear();
                    selectedCity = -1;

                    String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?" +
                            "key=" + getResources().getString(R.string.API_KEY_MAPS) + "&" +
                            "types=(cities)&" +
                            "input=" + city_search;

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
                            parseJsonForGettingCities(response.body().string());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                }
            }
        });

        btn_add_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String trip_name = tv_trip_name.getText().toString().trim();
                final String city_search = tv_city_search.getText().toString().trim();

                if (!trip_name.equals("") && !city_search.equals("") && selectedCity != -1) {

                    String url = "https://maps.googleapis.com/maps/api/place/details/json?" +
                            "key=" + getResources().getString(R.string.API_KEY_MAPS) +
                            "&placeid=" + placeIds.get(selectedCity);

                    Log.d("asdf", url);
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
                            parseJsonForGettingLatLag(response.body().string());
                            Log.d("asdf", "onResponse: something is wrong?");
                            Map<String, Object> map = new HashMap<>();
                            map.put("location", city_search);
                            map.put("trip_title", trip_name);
                            map.put("latlng_city", new GeoPoint(lat, lng));
                            map.put("place_id", placeIds.get(selectedCity));

                            db.collection("trips_test")
                                    .add(map)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("demo", "DocumentSnapshot written with ID: " + documentReference.getId());

                                            DocumentReference trip_id = documentReference;
                                            Map<String,Object> updates_id = new HashMap<>();
                                            updates_id.put("id", trip_id.getId());

                                            trip_id.update(updates_id)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("asdf", "DocumentSnapshot ka 'id' successfully updated!");
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("asdf", "Error updating document ka ID", e);
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("asdf", "Error adding document", e);
                                        }
                                    });

                        }
                    });

                } else {
                    Log.d("asdf", "else k inside" + selectedCity);
                    if (trip_name.equals("")) {
                        tv_trip_name.setError("Enter a trip name");
                    }
                    if (selectedCity == -1) {
                        tv_city_search.setError("Select a city");
                    }
                    if (city_search.equals("")) {
                        tv_city_search.setError("Enter a city");
                    }
                }
            }
        });

    }

    private void parseJsonForGettingCities(String s) {
        try {
            JSONObject root = new JSONObject(s);
            final String status = root.getString("status");

            JSONArray predictions = root.getJSONArray("predictions");

            for (int i = 0; i < predictions.length(); i++) {
                JSONObject obj = (JSONObject) predictions.get(i);
                String cityWithState = obj.getString("description");

                cityWithState = cityWithState.substring(0, cityWithState.lastIndexOf(","));
                data.add(cityWithState);
                String placeId = obj.getString("place_id");
                placeIds.add(placeId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonForGettingLatLag(String s) {
        try {
            JSONObject root = new JSONObject(s);
            final String status = root.getString("status");

            JSONObject obj = root.getJSONObject("result");
            JSONObject geometry = obj.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            lat = location.getDouble("lat");
            lng = location.getDouble("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
