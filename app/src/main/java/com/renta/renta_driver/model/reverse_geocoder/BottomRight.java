package com.renta.renta_driver.model.reverse_geocoder;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BottomRight implements Serializable {

	@SerializedName("Latitude")
	private double latitude;

	@SerializedName("Longitude")
	private double longitude;

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"BottomRight{" + 
			"latitude = '" + latitude + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}