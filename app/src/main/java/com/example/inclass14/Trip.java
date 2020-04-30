package com.example.inclass14;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

class Trip implements Parcelable {
    String id;
    String trip_title;
    String location;
    GeoPoint latlng_city;
    String place_id;
    ArrayList<Place> place = new ArrayList<>();

    public Trip() {

    }

    public static final Parcelable.Creator<Trip> CREATOR
            = new Parcelable.Creator<Trip>() {
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };


    private Trip(Parcel in) {
        id = in.readString();
        trip_title = in.readString();
        location = in.readString();
        place_id = in.readString();
        Bundle bundle = in.readBundle();
        latlng_city = new GeoPoint(bundle.getDouble("lat"), bundle.getDouble("lng"));
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

    public String getId() {
        return id;
    }

    public String getTrip_title() {
        return trip_title;
    }

    public String getLocation() {
        return location;
    }

    public GeoPoint getLatlng_city() {
        return latlng_city;
    }

    public String getPlace_id() {
        return place_id;
    }

    public ArrayList<Place> getPlace() {
        return place;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        dest.writeString(id);
        dest.writeString(trip_title);
        dest.writeString(location);
        dest.writeString(place_id);
        bundle.putDouble("lat", getLatlng_city().getLatitude());
        bundle.putDouble("lng", getLatlng_city().getLongitude());
        dest.writeBundle(bundle);
    }
}
