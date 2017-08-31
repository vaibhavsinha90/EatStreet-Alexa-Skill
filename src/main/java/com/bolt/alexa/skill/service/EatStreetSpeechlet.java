package com.bolt.alexa.skill.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.interfaces.system.SystemState;
import com.amazon.speech.ui.AskForPermissionsConsentCard;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.bolt.alexa.skill.model.Restaurant;

@Service
public class EatStreetSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(EatStreetSpeechlet.class);
    private static String defaultRadius="25";
    private static final String ALL_ADDRESS_PERMISSION = "read::alexa:device:all:address";
    @Autowired
	private RestaurantSearchService restaurantSearchService;
    
    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
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
	        	log.info("user perms here: "+session.getUser().getPermissions().toString());
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

                        AlexaDeviceAddressClient alexaDeviceAddressClient = new AlexaDeviceAddressClient(
                            deviceId, consentToken, apiEndpoint);

                        Address addressObject = alexaDeviceAddressClient.getFullAddress();

                        if (addressObject == null) {
                            return getAskResponse(ADDRESS_CARD_TITLE, ERROR_TEXT);
                        }

                        return getAddressResponse(
                            addressObject.getAddressLine1(),
                            addressObject.getStateOrRegion(),
                            addressObject.getPostalCode());
                    } catch (UnauthorizedException e) {
                        return getPermissionsResponse();
                    } catch (DeviceAddressClientException e) {
                        log.error("Device Address Client failed to successfully return the address.", e);
                        return getAskResponse(ADDRESS_CARD_TITLE, ERROR_TEXT);
                    }*/
                	return getRestaurantSearchResponse(intent);
                //}
	        case "AMAZON.HelpIntent":
	        	return getHelpResponse();
	        default:
	        	throw new SpeechletException("Invalid Intent");
        }
    }   

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }   
        
    

    private SpeechletResponse getRestaurantSearchResponse(Intent intent){
    	Map<String, Slot> intentSlots=intent.getSlots();
    	String speechText;// = "Hi, check the card!";
    	String showText;
        //for(Map.Entry<String, Slot> e:intentSlots.entrySet())
        //	showText+=e.getKey()+" : "+e.getValue().getName()+" : "+e.getValue().getValue();
    	boolean checkDelivery=!intent.getName().equals("IsOpen");
        Restaurant matchedRestaurant=restaurantSearchService.search(intentSlots.get("restaurant").getValue(),"112 Niagara Falls Blvd Buffalo NY",defaultRadius,checkDelivery);
        if(matchedRestaurant==null)
        	showText= "Restaurant not found";	
        else if(matchedRestaurant.isOpen()){
        	if(checkDelivery){
        		if(matchedRestaurant.isOffersDelivery())
                	showText=matchedRestaurant.getName()+" is open for delivery.";
        		else
        			showText=matchedRestaurant.getName()+" is open but won't deliver.";
        	}
        	else
        		showText=matchedRestaurant.getName()+" is open right now.";
        }
        else
        	showText="Sorry, "+matchedRestaurant.getName()+" is closed right now.";
        
        SimpleCard card = new SimpleCard();
        card.setTitle("EatStreet.com says...");
        card.setContent(showText);

        // Create the plain text output.
        speechText=showText;
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
    
    private SpeechletResponse getHelpResponse() {
        String speechText = "Hi, you can ask about restaurants on EatStreet.com";
        SimpleCard card = new SimpleCard();
        card.setTitle(speechText);
        card.setContent("You can ask if a restaurant is open/deliver to your house.");
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);
        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
    private SpeechletResponse getWelcomeResponse() {
        String speechText = "Hi, ask me about a restaurant.";
        SimpleCard card = new SimpleCard();
        card.setTitle("Ask details about a restaurant.");
        card.setContent(speechText);
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);
        return SpeechletResponse.newAskResponse(speech, reprompt, card);
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
    /*
	@RequestMapping(value="/WillDeliver/restaurant/{restaurant}", method = RequestMethod.GET)
	public String WillDeliverProcess(
        @PathVariable("restaurant") String restaurantName,
        @RequestParam(value = "moment", required = false) Date dateOrNull){
			Restaurant matchedRestaurant=restaurantSearchService.search(restaurantName,address,defaultRadius,true);
			if(matchedRestaurant==null)
				return "No restaurant found";
			else
				return matchedRestaurant.isOpen()? "Great! "+matchedRestaurant.getName()+" will deliver at the requested time."
						: "Sorry, "+matchedRestaurant.getName()+" not open at the time you requested.";
	}
	@RequestMapping(value="/IsOpen/restaurant/{restaurant}", method = RequestMethod.GET)
	public String IsOpenProcess(
        @PathVariable("restaurant") String restaurantName,
        @RequestParam(value = "moment", required = false) Date dateOrNull){
			Restaurant matchedRestaurant=restaurantSearchService.search(restaurantName);
			if(matchedRestaurant==null)
				return "No restaurant found";
			else
				return matchedRestaurant.isOpen()? "Great! "+matchedRestaurant.getName()+" is open at the requested time."
						: "Sorry, "+matchedRestaurant.getName()+" not open at the time you requested.";
	}
	*/
}
