package com.bolt.alexa.skill.model;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Component;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class Restaurant {
	String apiKey;
	String name;
	String url;
	String streetAddress;
	String city;
	String state;
	String zip;
	Double latitude;
	Double longtitude;
	String[] foodTypes;
	boolean offersDelivery;
	Double deliveryPrice;
	Double deliveryMin;
	Double minFreeDelivery;
	String logoUrl;
	boolean open;
	Map<String, String[]> hours;
	String timezone;
	DeliveryZone[] zones;
	@Override
	public String toString() {
		return "Restaurant [apiKey=" + apiKey + ", name=" + name + ", url=" + url + ", streetAddress=" + streetAddress
				+ ", city=" + city + ", state=" + state + ", zip=" + zip + ", latitude=" + latitude + ", longtitude="
				+ longtitude + ", foodTypes=" + Arrays.toString(foodTypes) + ", offersDelivery=" + offersDelivery
				+ ", deliveryPrice=" + deliveryPrice + ", deliveryMin=" + deliveryMin + ", minFreeDelivery="
				+ minFreeDelivery + ", logoUrl=" + logoUrl + ", open=" + open + ", hours=" + hours + ", timezone="
				+ timezone + ", zones=" + Arrays.toString(zones) + "]";
	}
	public Restaurant(){
		
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}
	public String[] getFoodTypes() {
		return foodTypes;
	}
	public void setFoodTypes(String[] foodTypes) {
		this.foodTypes = foodTypes;
	}
	public boolean isOffersDelivery() {
		return offersDelivery;
	}
	public void setOffersDelivery(boolean offersDelivery) {
		this.offersDelivery = offersDelivery;
	}
	public Double getDeliveryPrice() {
		return deliveryPrice;
	}
	public void setDeliveryPrice(Double deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}
	public Double getDeliveryMin() {
		return deliveryMin;
	}
	public void setDeliveryMin(Double deliveryMin) {
		this.deliveryMin = deliveryMin;
	}
	public Double getMinFreeDelivery() {
		return minFreeDelivery;
	}
	public void setMinFreeDelivery(Double minFreeDelivery) {
		this.minFreeDelivery = minFreeDelivery;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public Map<String, String[]> getHours() {
		return hours;
	}
	public void setHours(Map<String, String[]> hours) {
		this.hours = hours;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public DeliveryZone[] getZones() {
		return zones;
	}
	public void setZones(DeliveryZone[] zones) {
		this.zones = zones;
	}
}
