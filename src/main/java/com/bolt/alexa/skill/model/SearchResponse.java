package com.bolt.alexa.skill.model;

import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class SearchResponse{
		private ESAddress eSAddress;
		private Restaurant restaurants[];
		public ESAddress getAddress() {
			return eSAddress;
		}
		public void setAddress(ESAddress eSAddress) {
			this.eSAddress = eSAddress;
		}
		public Restaurant[] getRestaurants() {
			return restaurants;
		}
		public void setRestaurants(Restaurant[] restaurants) {
			this.restaurants = restaurants;
		}
		@Override
		public String toString() {
			return "SearchResponse [eSAddress=" + eSAddress + ", restaurants=" + Arrays.toString(restaurants) + "]";
		}
	}