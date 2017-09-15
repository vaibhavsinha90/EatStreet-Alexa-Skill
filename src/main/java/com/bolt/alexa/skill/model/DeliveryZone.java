package com.bolt.alexa.skill.model;

import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class DeliveryZone {
	String apiKey;
	String description;
	String[] zips;
	LatLonPoint[] points;
	LatLonPoint[] holePoints;
	Double maximumRadius;
	
	@Override
	public String toString() {
		return "DeliveryZone [apiKey=" + apiKey + ", description=" + description + ", zips=" + Arrays.toString(zips)
				+ ", points=" + Arrays.toString(points) + ", holePoints=" + Arrays.toString(holePoints)
				+ ", maximumRadius=" + maximumRadius + "]";
	}
	public DeliveryZone(){
		
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getZips() {
		return zips;
	}
	public void setZips(String[] zips) {
		this.zips = zips;
	}
	public LatLonPoint[] getPoints() {
		return points;
	}
	public void setPoints(LatLonPoint[] points) {
		this.points = points;
	}
	public LatLonPoint[] getHolePoints() {
		return holePoints;
	}
	public void setHolePoints(LatLonPoint[] holePoints) {
		this.holePoints = holePoints;
	}
	public Double getMaximumRadius() {
		return maximumRadius;
	}
	public void setMaximumRadius(Double maximumRadius) {
		this.maximumRadius = maximumRadius;
	}
	
}
