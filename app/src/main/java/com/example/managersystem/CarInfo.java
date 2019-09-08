package com.example.managersystem;

import java.io.Serializable;

public class CarInfo implements Serializable {

    private Integer id;
    private String name;
    private String number;
    private double lng;
    private double lat;

    public CarInfo(Integer id, String name, String number, double lng, double lat) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.lng = lng;
        this.lat = lat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "CarInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
