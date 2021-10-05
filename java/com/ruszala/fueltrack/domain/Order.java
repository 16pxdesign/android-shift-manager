package com.ruszala.fueltrack.domain;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * Model class represent order
 */
public class Order implements Serializable {

    private String id;
    private String shiftId;
    private String adders;
    private String phone;
    private Double price;
    private boolean cash;
    private boolean delivered;
    private LatLng position;
    private Double latitude;
    private Double longitude;

    public Order() {
        adders = "";
        phone = "";
        price = 0.00;
        cash = false;
        latitude = 56.464621;
        longitude = -2.974309;
        position = new LatLng(latitude, longitude);
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdders() {
        return adders;
    }

    public void setAdders(String adders) {
        this.adders = adders;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Exclude
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @Exclude
    public void setPosition(LatLng position) {
        latitude = position.latitude;
        longitude = position.longitude;
    }

    public boolean isCash() {
        return cash;
    }

    public void setCash(boolean cash) {
        this.cash = cash;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    @Exclude
    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    @Exclude
    public String getCardString() {
        if (cash) {
            return "Cash";
        } else {
            return "Card";
        }
    }

    @Exclude
    public String getPriceString() {
        return String.valueOf(price);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    //Check is passing order is same
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Order) {
            Order p = (Order) obj;
            return this.id.equals(p.getId());
        } else
            return false;
    }
}

