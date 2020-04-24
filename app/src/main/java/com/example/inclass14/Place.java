package com.example.inclass14;

import com.google.type.LatLng;

class Place {
    String name;
    String img_url;
    double[] latlng = new double[2];

    public Place(String name, String img_url, double... latlng) {
        this.name = name;
        this.img_url = img_url;
        this.latlng[0] = latlng[0];
        this.latlng[1] = latlng[1];
    }

    @Override
    public String toString() {
        return "Place{" +
                "img_url='" + img_url + '\'' +
                ", name='" + name + '\'' +
                ", latLng=" + latlng +
                '}';
    }
}
