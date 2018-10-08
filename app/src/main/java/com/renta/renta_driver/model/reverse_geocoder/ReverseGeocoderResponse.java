package com.renta.renta_driver.model.reverse_geocoder;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReverseGeocoderResponse implements Serializable {

    @SerializedName("Response")
    private ReverseGeocoder reverseGeocoder;


    public ReverseGeocoder getReverseGeocoder() {
        return reverseGeocoder;
    }

    public void setReverseGeocoder(ReverseGeocoder reverseGeocoder) {
        this.reverseGeocoder = reverseGeocoder;
    }

    public Locations getLocationDetails(){
        return reverseGeocoder.getView().get(0).getResult().get(0).getLocations();
    }
}
