package com.bolt.alexa.skill.model;

import org.springframework.stereotype.Component;

@Component
public class LatLonPoint {
	Double latitude;
	Double longitude;
	public LatLonPoint(){
		
	}
	
	@Override
	public String toString() {
		return "LatLonPoint [latitude=" + latitude + ", longitude=" + longitude + "]";
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
}
