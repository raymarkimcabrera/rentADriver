package com.renta.renta_driver.model.reverse_geocoder;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Address implements Serializable{

	@SerializedName("AdditionalData")
	private List<AdditionalDataItem> additionalData;

	@SerializedName("HouseNumber")
	private String houseNumber;

	@SerializedName("State")
	private String state;

	@SerializedName("Label")
	private String label;

	@SerializedName("Country")
	private String country;

	@SerializedName("Street")
	private String street;

	@SerializedName("PostalCode")
	private String postalCode;

	@SerializedName("City")
	private String city;

	@SerializedName("County")
	private String county;

	@SerializedName("District")
	private String district;

	public void setAdditionalData(List<AdditionalDataItem> additionalData){
		this.additionalData = additionalData;
	}

	public List<AdditionalDataItem> getAdditionalData(){
		return additionalData;
	}

	public void setHouseNumber(String houseNumber){
		this.houseNumber = houseNumber;
	}

	public String getHouseNumber(){
		if (houseNumber != null){
			return houseNumber;
		}
		return "";
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setLabel(String label){
		this.label = label;
	}

	public String getLabel(){
		return label;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setStreet(String street){
		this.street = street;
	}

	public String getStreet(){
		if (street != null){
			return street;
		}
		return "";
	}

	public void setPostalCode(String postalCode){
		this.postalCode = postalCode;
	}

	public String getPostalCode(){
		return postalCode;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		if (city != null){
			return city;
		}
		return "";
	}

	public void setCounty(String county){
		this.county = county;
	}

	public String getCounty(){
		return county;
	}

	public void setDistrict(String district){
		this.district = district;
	}

	public String getDistrict(){
		if (district != null){
			return district;
		}
		return "";
	}

	public String getFullAddress(){
		return getHouseNumber() + " " + getStreet() + " " + getDistrict() + " "+ getCity();
	}

	@Override
 	public String toString(){
		return 
			"Address{" + 
			"additionalData = '" + additionalData + '\'' + 
			",houseNumber = '" + houseNumber + '\'' + 
			",state = '" + state + '\'' + 
			",label = '" + label + '\'' + 
			",country = '" + country + '\'' + 
			",street = '" + street + '\'' + 
			",postalCode = '" + postalCode + '\'' + 
			",city = '" + city + '\'' + 
			",county = '" + county + '\'' + 
			",district = '" + district + '\'' + 
			"}";
		}
}