package com.kaisizhenghw9.hw9;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {
    private String name;
    private String address;
    private String phoneNumber;
    private String id;
    private Uri websiteUri;
    private LatLng latLng;
    private float rating;
    private String attributions;
    public PlaceInfo() {

    }
    public PlaceInfo(String name, String address, String phoneNumber, String id, Uri websiteUri, LatLng latLng, float rating, String attributions) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.websiteUri = websiteUri;
        this.latLng = latLng;
        this.rating = rating;
        this.attributions = attributions;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getId() {
        return id;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public float getRating() {
        return rating;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    @Override
    public String toString() {
        return "StartInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", websiteUri=" + websiteUri +
                ", latLng=" + latLng +
                ", rating=" + rating +
                ", attributions='" + attributions + '\'' +
                '}';
    }
}
