package com.example.inclass14;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.HashMap;

class Place implements Parcelable {
    String trip_id;
    String place_id;
    String name;
    String img_url;
    GeoPoint latlng_place;

    public Place() {
    }

    protected Place(Parcel in) {
        trip_id = in.readString();
        place_id = in.readString();
        name = in.readString();
        img_url = in.readString();
        Bundle bundle = in.readBundle();
        latlng_place = new GeoPoint(bundle.getDouble("lat"), bundle.getDouble("lng"));
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public String toString() {
        return "Place{" +
                "id='" + trip_id + '\'' +
                ", place_id='" + place_id + '\'' +
                ", name='" + name + '\'' +
                ", img_url='" + img_url + '\'' +
                ", latlng_place=" + latlng_place +
                '}';
    }

    HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("trip_id", trip_id);
        map.put("place_id", place_id);
        map.put("name", name);
        map.put("img_url", img_url);
        map.put("latlng_place", latlng_place);

        return map;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getName() {
        return name;
    }

    public String getImg_url() {
        return img_url;
    }

    public GeoPoint getLatlng_place() {
        return latlng_place;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        dest.writeString(trip_id);
        dest.writeString(place_id);
        dest.writeString(name);
        dest.writeString(img_url);
        bundle.putDouble("lat", getLatlng_place().getLatitude());
        bundle.putDouble("lng", getLatlng_place().getLongitude());
        dest.writeBundle(bundle);
    }
}