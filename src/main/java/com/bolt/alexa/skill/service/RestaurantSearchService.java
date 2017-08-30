package com.bolt.alexa.skill.service;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.bolt.alexa.skill.model.Restaurant;
import com.bolt.alexa.skill.model.SearchResponse;

@Service
public class RestaurantSearchService {
	private static String url="https://api.eatstreet.com";
	private static String accessToken="c5f5a4f0efbb2a10";
	private static Logger log = LoggerFactory.getLogger(RestaurantSearchService.class);
	private String method;
	@Autowired
	SearchResponse response;
	@Autowired
	RestTemplate restTemplate;
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		log.info(" yoyoyoyoyyo");
		return builder.build();
	}
	public Restaurant search(String restaurantName,String address,String pickupRadius,boolean checkDelivery) {
		if(checkDelivery)
			method="delivery";
		else
			method="both";		
		
		log.info("before CLR");
		MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();
		vars.add("access-token", accessToken);
		vars.add("method", method);
		vars.add("street-address", address);
		vars.add("pickup-radius",pickupRadius);
		URI targetUrl= UriComponentsBuilder.fromUriString(url)
			    .path("/publicapi/v1/restaurant/search/")
			    .queryParams(vars)
			    .build()
			    .encode()
			    .toUri();
		response=restTemplate.getForObject(targetUrl, SearchResponse.class);
		for(Restaurant r :response.getRestaurants()){
			if(similar(trimLocation(r.getName()),restaurantName)){
				log.info(r.getName());
			}
		}	
	    log.info("after CLR");
		
		return null;
	}

	private boolean similar(Object trimLocation, String restaurantName) {
		
		return false;
	}

	private Object trimLocation(String name) {
		
		return null;
	}

}
