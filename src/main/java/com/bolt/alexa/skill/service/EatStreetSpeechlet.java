package com.bolt.alexa.skill.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.interfaces.system.SystemInterface;
import com.amazon.speech.speechlet.interfaces.system.SystemState;
import com.amazon.speech.ui.AskForPermissionsConsentCard;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.bolt.alexa.skill.Amazonmodel.Address;
import com.bolt.alexa.skill.exception.DeviceAddressClientException;
import com.bolt.alexa.skill.exception.UnauthorizedException;
import com.bolt.alexa.skill.model.RestaurantSearchResponse;
import com.bolt.alexa.skill.model.Restaurant;

@Service
public class EatStreetSpeechlet implements SpeechletV2 {
    private static final Logger log = LoggerFactory.getLogger(EatStreetSpeechlet.class);
    private static String defaultRadius="25";
    private static final String ALL_ADDRESS_PERMISSION = "read::alexa:device:all:address";
    private static final String ERROR_TEXT="There was an error with the skill. Please try again.";
    private static final String CARD_TITLE="EatStreet.com says...";
    private static final String SKILL_NAME="Get fed on EatStreet";
<<<<<<< HEAD
    private static final String RePrompt="Ask for help or, try asking another question now.";
    private static final String HELP_TEXT="You can  ask the skill to find an EatStreet partner restaurant near you, based on cuisine or food items."
    		+ " You can also ask if an EatStreet partner restaurant near you, is open, or whether it will deliver to you."
    		+ " If a restaurant would deliver to you, the skill will tell you the associated minimum order value and delivery charges."
    		+ " If the requested restaurant is not found by the skill, it may provide a few alternatives."
    		+ " Try asking a question now.";
    private static final String INVALID_INTENT_TEXT="This is unsupported. Please ask something else.";
    private static final String WELCOME_TEXT="Ask me about a restaurant.";
=======
    private static final String HELP_TEXT="You can ask if a EatStreet partner restaurant near you, is open, or whether it will deliver to you."
    		+ " If a restaurant would deliver to you, the skill will tell you the associated minimum order value and delivery charges."
    		+ " If the requested restaurant is not found by the skill, it may provide a few alternatives."
    		//+ " For a restaurant named Sam's Cafe, ask, Will Sam's Cafe deliver?"
    		+ " Try asking a question now.";
    private static final String INVALID_INTENT_TEXT="This is unsupported. Please ask something else.";
    private static final String WELCOME_TEXT="Hi, ask me about a restaurant.";
>>>>>>> e0c54fb50a2ce90cff54b36f30ac7f1368d5766b
    
    @Autowired
	private RestaurantSearchService restaurantSearchService;
    
    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> speechletRequestEnvelope) {
        SessionStartedRequest request = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }
    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> speechletRequestEnvelope) {
        LaunchRequest request = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getAskResponse("Welcome to the "+SKILL_NAME+" skill!",WELCOME_TEXT);
    }

    @Override
    
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelope) {
        IntentRequest request = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        
        switch(intentName){
	        case "WillDeliver":
	        case "IsOpen":
<<<<<<< HEAD
	        case "FindFood":
	        	
=======
	        	/*
	        	log.info("user here: "+session.getUser().toString());
	        	log.info("userID here: "+session.getUser().getUserId().toString());
	        	log.info("user perms here: "+session.getUser().getPermissions().toString());*/
>>>>>>> e0c54fb50a2ce90cff54b36f30ac7f1368d5766b
	        	String consentToken = session.getUser().getPermissions().getConsentToken();
                if (consentToken == null) {
                    log.info("The user hasn't authorized the skill. Sending a permissions card.");
                    return getPermissionsResponse();
                }
                else{
                	try {
                        SystemState systemState = getSystemState(speechletRequestEnvelope.getContext());

                        String deviceId = systemState.getDevice().getDeviceId();
                        String apiEndpoint = systemState.getApiEndpoint();
<<<<<<< HEAD
=======
                        //log.info(deviceId+" & "+apiEndpoint);
>>>>>>> e0c54fb50a2ce90cff54b36f30ac7f1368d5766b
                        AlexaDeviceAddressClient alexaDeviceAddressClient = new AlexaDeviceAddressClient(deviceId, consentToken, apiEndpoint);

                        Address deviceAddress = alexaDeviceAddressClient.getFullAddress();

                        if (deviceAddress == null) 
                            return getAskResponse(CARD_TITLE, ERROR_TEXT);
                        log.info(deviceAddress.toString());

                    	return getRestaurantSearchResponse(intent,deviceAddress);
                    } catch (UnauthorizedException e) {
                        return getPermissionsResponse();
                    } catch (DeviceAddressClientException e) {
                        log.error("Device Address Client failed to successfully return the address.", e);
                        return getAskResponse(CARD_TITLE, ERROR_TEXT);
                    }
                }
	        case "AMAZON.HelpIntent":
<<<<<<< HEAD
	        	return getAskResponse("You can find restaurants by cuisine, or ask if a restaurant is open/will deliver to your house.", HELP_TEXT);
=======
	        	return getAskResponse("You can ask if a restaurant is open/will deliver to your house.", HELP_TEXT);
>>>>>>> e0c54fb50a2ce90cff54b36f30ac7f1368d5766b
	        case "AMAZON.StopIntent":
	        case "AMAZON.CancelIntent":
	        	return getTellResponse("");
	        default:
<<<<<<< HEAD
	        	return getAskResponse("You can find restaurants by cuisine, or ask if a restaurant is open/will deliver to your house.",INVALID_INTENT_TEXT);
=======
	        	return getAskResponse("You can ask if a restaurant is open/will deliver to your house.",INVALID_INTENT_TEXT);
>>>>>>> e0c54fb50a2ce90cff54b36f30ac7f1368d5766b
        }
    }   

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> speechletRequestEnvelope) {
        SessionEndedRequest request = speechletRequestEnvelope.getRequest();
        Session session = speechletRequestEnvelope.getSession();
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }
    
    private SpeechletResponse getRestaurantSearchResponse(Intent intent,Address deviceAddress){
    	Map<String, Slot> intentSlots=intent.getSlots();
    	String speechText;
    	String showText;
<<<<<<< HEAD
        boolean checkDelivery=intent.getName().equals("WillDeliver");
    	String deviceAddressText=deviceAddress.getTextAdddress();
    	
    	String findRestaurant=null,findFood=null;
    	boolean findFoodIntent=intent.getName().equals("FindFood");
    	if(findFoodIntent){
    		log.info("find by food");
    		findFood=intentSlots.get("queryTerm").getValue();
    		if(findFood==null)
    			return getAskResponse("You can find restaurants by cuisine, or ask if a restaurant is open/will deliver to your house.","You did not provide a search term. "+RePrompt);
    	}	
    	else{
    		log.info("find by restaurant");
    		findRestaurant=intentSlots.get("restaurant").getValue();
    		if(findRestaurant==null)
    			return getAskResponse("You can find restaurants by cuisine, or ask if a restaurant is open/will deliver to your house.","You did not provide a restaurant name. "+RePrompt);
    	}
    		
    	
    	log.info("Device Address= "+deviceAddressText+"will check eatstreet now");
        RestaurantSearchResponse response=restaurantSearchService.search(findRestaurant,findFood,deviceAddressText,defaultRadius,checkDelivery);
        Restaurant[] foundRestaurants=response.getFoundRestaurants();
        if(!response.isFoundMatch()){
        	if(findFoodIntent){
        		showText="Sorry, no restaurant on EatStreet.com near you serves "+findFood;
        	}
        	else{
        		showText="Sorry, "+findRestaurant+" was not found near you on EatStreet.com.";
            	if(foundRestaurants.length==0)
            		showText+=" Currently, EatStreet does not partner with any restaurants near you.";
            	else if(foundRestaurants.length==1)
            		showText+=" Another restaurant I found is "+foundRestaurants[0].getName();
            	else{
            		showText+=" Some other restaurants I found are - ";
            		showText+=addRestaurantNames(foundRestaurants);
            	}
        	}
        }	
        else{
        	if(findFoodIntent){
        		showText="You could try ";
        		if(foundRestaurants.length==1)
            		showText+=foundRestaurants[0].getName()+".";
            	else
            		showText+=addRestaurantNames(foundRestaurants);
        	}
	        else{
	        	Restaurant matchedRestaurant=foundRestaurants[0];
	        	if(matchedRestaurant.isOpen()){
	            	if(checkDelivery){
	            		if(matchedRestaurant.isOffersDelivery()){
	            			StringBuilder temp_sb=new StringBuilder();
	            			temp_sb.append(matchedRestaurant.getName()+" will deliver to your location");
	            			if(matchedRestaurant.getDeliveryMin()!=null)
	            				temp_sb.append(" on a minimum order of "+matchedRestaurant.getDeliveryMin());
	            			if(matchedRestaurant.getDeliveryPrice()!=null)
	            				temp_sb.append(" . Delivery charge is "+matchedRestaurant.getDeliveryPrice());
	            			showText=temp_sb.toString()+".";
	            		}	
	            		else
	            			showText=matchedRestaurant.getName()+" is open but does not deliver to your location.";
	            	}
	            	else
	            		showText=matchedRestaurant.getName()+" is open right now.";
	            }
	            else
	            	showText="Sorry, "+matchedRestaurant.getName()+" is closed right now.";
	        }
=======
        //for(Map.Entry<String, Slot> e:intentSlots.entrySet())
        //	showText+=e.getKey()+" : "+e.getValue().getName()+" : "+e.getValue().getValue();
    	boolean checkDelivery=!intent.getName().equals("IsOpen");
    	String deviceAddressText=deviceAddress.getTextAdddress();
    	String findRestaurant=intentSlots.get("restaurant").getValue();
    	log.info("Device Address= "+deviceAddressText+"will check eatstreet now");
        RestaurantSearchResponse response=restaurantSearchService.search(findRestaurant,deviceAddressText,defaultRadius,checkDelivery);
        Restaurant[] foundRestaurants=response.getFoundRestaurants();
        if(!response.isFoundMatch()){
        	showText="Sorry, "+findRestaurant+" was not found near you on EatStreet.com.";
        	if(foundRestaurants.length==0)
        		showText+=" Currently, EatStreet does not partner with any restaurants near you.";
        	else if(foundRestaurants.length==1)
        		showText+=" Another restaurant I found is "+foundRestaurants[0].getName();
        	else{
        		showText+=" Some other restaurants I found are - ";
        		int i=0;
            	for(;i<foundRestaurants.length-2;i++)
            		showText+=foundRestaurants[i].getName()+", ";
            	showText+=foundRestaurants[i].getName()+", and "+foundRestaurants[i+1].getName();
        	}
        }	
        else{
        	Restaurant matchedRestaurant=foundRestaurants[0];
        	if(matchedRestaurant.isOpen()){
            	if(checkDelivery){
            		if(matchedRestaurant.isOffersDelivery()){
            			StringBuilder temp_sb=new StringBuilder();
            			temp_sb.append(matchedRestaurant.getName()+" will deliver to your location");
            			if(matchedRestaurant.getDeliveryMin()!=null)
            				temp_sb.append(" on a minimum order of "+matchedRestaurant.getDeliveryMin());
            			if(matchedRestaurant.getDeliveryMin()!=null)
            				temp_sb.append(" . Delivery charge is "+matchedRestaurant.getDeliveryPrice());
            			showText=temp_sb.toString()+".";
            		}	
            		else
            			showText=matchedRestaurant.getName()+" is open but does not deliver to your location.";
            	}
            	else
            		showText=matchedRestaurant.getName()+" is open right now.";
            }
            else
            	showText="Sorry, "+matchedRestaurant.getName()+" is closed right now.";
>>>>>>> e0c54fb50a2ce90cff54b36f30ac7f1368d5766b
        }
        
        SimpleCard card = new SimpleCard();
        card.setTitle(CARD_TITLE);
        card.setContent(showText);

        speechText=showText;
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
    private SpeechletResponse getAskResponse(String cardContent, String speechText) {
    	SimpleCard card = new SimpleCard();
        card.setTitle(CARD_TITLE);
        card.setContent(cardContent);
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);
        return SpeechletResponse.newAskResponse(speech, reprompt, card);    
    }
    private SpeechletResponse getTellResponse(String speechText) {
    	//log.info("getTellSpeechletResponse function called");
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
    private SpeechletResponse getPermissionsResponse() {
        String speechText = "You have not given this skill permissions to access your address. " +
            "Please give this skill permissions to access your address.";

        AskForPermissionsConsentCard card = new AskForPermissionsConsentCard();
        card.setTitle("The "+SKILL_NAME+" skill needs your address");

        Set<String> permissions = new HashSet<>();
        permissions.add(ALL_ADDRESS_PERMISSION);
        card.setPermissions(permissions);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
<<<<<<< HEAD
    private String addRestaurantNames(Restaurant[] restaurants){
    	StringBuilder temp_sb=new StringBuilder();
    	int i=0;
    	for(;i<restaurants.length-2;i++)
    		temp_sb.append(restaurants[i].getName()+", ");
    	temp_sb.append(restaurants[i].getName()+", and "+restaurants[i+1].getName()+".");
    	return temp_sb.toString();
    }
=======
>>>>>>> e0c54fb50a2ce90cff54b36f30ac7f1368d5766b
    private SystemState getSystemState(Context context) {
        return context.getState(SystemInterface.class, SystemState.class);
    }
}
