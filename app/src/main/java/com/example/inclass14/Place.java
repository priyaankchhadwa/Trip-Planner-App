package com.example.inclass14;

import com.google.firebase.firestore.GeoPoint;
import com.google.type.LatLng;

import java.util.Arrays;

class Place {
    String id;
    String place_id;
    String name;
    String img_url;
    GeoPoint latlng_place;

    public Place() {
    }
}