package com.example.inclass14;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private TextView tv_title_path;

    private ArrayList<LatLng> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        tv_title_path = findViewById(R.id.tv_title_path);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        Trip trip = getIntent().getParcelableExtra("trip");
        final ArrayList<Place> places = getIntent().getParcelableArrayListExtra("places");
        tv_title_path.setText(trip.trip_title);

        Log.d("asdf", "inside map activity " + trip.getLatlng_city().getLatitude() +  trip.getLatlng_city().getLongitude());

        final LatLng t = new LatLng(trip.getLatlng_city().getLatitude(), trip.getLatlng_city().getLongitude());
        this.points.add(t);
        mMap.addMarker(new MarkerOptions().position(t).title(trip.location));

        Log.d("asdf", "inside map activity trip obj places = " + places);

        if (!places.isEmpty()) {
            for (Place place: places) {

                LatLng latLng = new LatLng(place.getLatlng_place().getLatitude(), place.getLatlng_place().getLongitude());
                this.points.add(latLng);
                mMap.addMarker(new MarkerOptions().position(latLng).title(place.name));

                LatLngBounds.Builder bounds = new LatLngBounds.Builder();

                for (LatLng l: this.points) {
                    bounds.include(l);
                }

                final LatLngBounds latLngBound = bounds.build();

                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBound, 256));
                    }
                });
            }
        } else {
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(t, 12));
                }
            });
        }



//        mMap.moveCamera(CameraUpdateFactory.newLatLng(t));
    }
}
