package com.kaisizhenghw9.hw9;

public class PlacesItem  {

    private String icon;
    private String name;
    private String address;
    private String place_id;
    public PlacesItem(String icon, String name, String address, String place_id) {
        this.icon = icon;
        this.name = name;
        this.address = address;
        this.place_id = place_id;
    }
    public String getIcon() {
       return icon;
    }

    public String getAddress() {
        return address;
    }

    public String getPlace_id() {
        return place_id;
    }
    public String getName() {
        return name;
    }
}
