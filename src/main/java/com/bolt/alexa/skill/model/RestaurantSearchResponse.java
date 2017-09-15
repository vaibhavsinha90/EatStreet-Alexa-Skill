package com.bolt.alexa.skill.model;


public class RestaurantSearchResponse{
	private final boolean foundMatch;
	private final Restaurant[] foundRestaurants;
	public RestaurantSearchResponse(boolean foundMatch, Restaurant[] foundRestaurants) {
		super();
		this.foundMatch = foundMatch;
		this.foundRestaurants = foundRestaurants;
	}
	public boolean isFoundMatch() {
		return foundMatch;
	}
	public Restaurant[] getFoundRestaurants() {
		return foundRestaurants;
	}
}