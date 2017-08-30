package com.bolt.alexa.skill.model;

import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class SearchResponse{
		private Address address;
		private Restaurant restaurants[];
		public Address getAddress() {
			return address;
		}
		public void setAddress(Address address) {
			this.address = address;
		}
		public Restaurant[] getRestaurants() {
			return restaurants;
		}
		public void setRestaurants(Restaurant[] restaurants) {
			this.restaurants = restaurants;
		}
		@Override
		public String toString() {
			return "SearchResponse [address=" + address + ", restaurants=" + Arrays.toString(restaurants) + "]";
		}
	}