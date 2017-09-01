package com.bolt.alexa.skill.service;

import java.net.URI;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.bolt.alexa.skill.Amazonmodel.Address;
import com.bolt.alexa.skill.model.Restaurant;
import com.bolt.alexa.skill.model.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestaurantSearchService {
	private static final Integer SIMILARITY_THRESHOLD = 4;
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
		return builder.build();
	}
	public Restaurant search(String restaurantName,String address,String pickupRadius,boolean checkDelivery) {
		if(checkDelivery)
			method="delivery";
		else
			method="both";		
		
		log.info("Will search restaurant now...");
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
		//log.info("Will check URL:   "+targetUrl.toString());
		
		try {
			response=restTemplate.getForObject(targetUrl, SearchResponse.class);
			for(Restaurant r :response.getRestaurants()){
				if(similar(trimLocation(r.getName()),restaurantName)){
					log.info("Restaurant found!");
					return r;
				}
			}           
		}
		catch(Exception e){
			log.info("Call to EatStreet API failed!");
		}
		return null;
	}

	private boolean similar(String trimLocation, String restaurantName) {
		LevenshteinDistance t=new LevenshteinDistance(SIMILARITY_THRESHOLD);
		return t.apply(trimLocation,restaurantName)!=-1;
	}

	private String trimLocation(String name) {
		return name.replaceAll(" - .+", "");
	}

}
