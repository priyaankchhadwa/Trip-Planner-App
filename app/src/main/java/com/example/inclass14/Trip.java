package com.example.inclass14;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

class Trip {
    String id;
    String trip_title;
    String location;
    GeoPoint latlng_city;
    String place_id;
    ArrayList<Place> place = new ArrayList<>();

    public Trip() {

    }

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", trip_title='" + trip_title + '\'' +
                ", location='" + location + '\'' +
                ", latlng_city=" + latlng_city +
                ", place_id='" + place_id + '\'' +
                ", place=" + place +
                '}';
    }
}
