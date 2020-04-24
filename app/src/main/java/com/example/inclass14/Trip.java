package com.example.inclass14;

import java.util.ArrayList;

class Trip {
    String trip_title, Location;
    ArrayList<Place> place;

    public Trip(String trip_title, String location, ArrayList<Place> place) {
        this.trip_title = trip_title;
        Location = location;
        this.place = place;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "trip_title='" + trip_title + '\'' +
                ", Location='" + Location + '\'' +
                ", place=" + place +
                '}';
    }
}
