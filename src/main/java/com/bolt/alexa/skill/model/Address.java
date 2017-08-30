package com.bolt.alexa.skill.model;

import org.springframework.stereotype.Component;

@Component
public class Address {
	String apiKey;
	String streetAddress;
	String city;
	String state;
	String zip;
	String aptNumber;
	String latitude;
	String longitude;
	
	@Override
	public String toString() {
		return "Address [apiKey=" + apiKey + ", streetAddress=" + streetAddress + ", city=" + city + ", state=" + state
				+ ", zip=" + zip + ", aptNumber=" + aptNumber + ", latitude=" + latitude + ", longitude=" + longitude
				+ "]";
	}
	public Address(){
		
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getAptNumber() {
		return aptNumber;
	}
	public void setAptNumber(String aptNumber) {
		this.aptNumber = aptNumber;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
