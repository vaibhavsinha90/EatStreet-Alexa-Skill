package com.bolt.alexa.skill.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.speech.Sdk;
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
import com.amazon.speech.speechlet.verifier.TimestampSpeechletRequestVerifier;
import com.amazon.speech.ui.AskForPermissionsConsentCard;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.bolt.alexa.skill.Amazonmodel.Address;
import com.bolt.alexa.skill.exception.DeviceAddressClientException;
import com.bolt.alexa.skill.exception.UnauthorizedException;
import com.bolt.alexa.skill.model.Restaurant;

@Service
public class EatStreetSpeechlet implements SpeechletV2 {
    private static final Logger log = LoggerFactory.getLogger(EatStreetSpeechlet.class);
    private static String defaultRadius="25";
    private static final String ALL_ADDRESS_PERMISSION = "read::alexa:device:all:address";
    private static final String ERROR_TEXT="There was an error with the skill. Please try again.";
    private static final String CARD_TITLE="EatStreet.com says...";
    private static final String HELP_TEXT="Hi, you can ask about restaurants on EatStreet.com";
    private static final String INVALID_INTENT_TEXT="This is unsupported. Please ask something else.";
    private static final String WELCOME_TEXT="Hi, ask me about a restaurant.";
    
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
        return getAskResponse("Welcome to the Echo on EatStreet skill!",WELCOME_TEXT);
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
	        	/*
	        	log.info("user here: "+session.getUser().toString());
	        	log.info("userID here: "+session.getUser().getUserId().toString());
	        	log.info("user perms here: "+session.getUser().getPermissions().toString());*/
	        	
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
                        //log.info(deviceId+" & "+apiEndpoint);
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
	        	return getAskResponse("You can ask if a restaurant is open/will deliver to your house.", HELP_TEXT);
	        case "AMAZON.StopIntent":
	        case "AMAZON.CancelIntent":
	        	return getTellResponse("");
	        	//getAskResponse("You can ask if a restaurant is open/will deliver to your house.", HELP_TEXT);
	        default:
	        	return getAskResponse("You can ask if a restaurant is open/will deliver to your house.",INVALID_INTENT_TEXT);
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
        //for(Map.Entry<String, Slot> e:intentSlots.entrySet())
        //	showText+=e.getKey()+" : "+e.getValue().getName()+" : "+e.getValue().getValue();
    	boolean checkDelivery=!intent.getName().equals("IsOpen");
    	String deviceAddressText=deviceAddress.getTextAdddress();
    	log.info("Device Address= "+deviceAddressText+"will check eatstreet now");
        Restaurant matchedRestaurant=restaurantSearchService.search(intentSlots.get("restaurant").getValue(),deviceAddressText,defaultRadius,checkDelivery);
        if(matchedRestaurant==null)
        	showText= "Restaurant not found";	
        else if(matchedRestaurant.isOpen()){
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
        card.setTitle("EatStreet Skill needs your address");

        Set<String> permissions = new HashSet<>();
        permissions.add(ALL_ADDRESS_PERMISSION);
        card.setPermissions(permissions);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
    private SystemState getSystemState(Context context) {
        return context.getState(SystemInterface.class, SystemState.class);
    }
    
    /*
    private TimestampSpeechletRequestVerifier getTimetampVerifier() {
        String timestampToleranceAsString =
                System.getProperty(Sdk.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY);
 
        if (timestampToleranceAsString==null ||timestampToleranceAsString.length()==0) {
            try {
                long timestampTolerance = Long.parseLong(timestampToleranceAsString);
                return new TimestampSpeechletRequestVerifier(timestampTolerance, TimeUnit.SECONDS);
            } catch (NumberFormatException ex) {
                log.warn("The configured timestamp tolerance {} is invalid, "
                        + "disabling timestamp verification", timestampToleranceAsString);
            }
        } else {
            log.warn("No timestamp tolerance has been configured, "
                    + "disabling timestamp verification");
        }
 
        return null;
    }*/
}
