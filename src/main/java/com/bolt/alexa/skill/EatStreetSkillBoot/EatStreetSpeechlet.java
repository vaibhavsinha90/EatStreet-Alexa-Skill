package com.bolt.alexa.skill.EatStreetSkillBoot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.bolt.alexa.skill.service.RestaurantSearchService;

@Service
public class EatStreetSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(EatStreetSpeechlet.class);
    private static String defaultRadius="25";
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

        if ("HelloWorldIntent".equals(intentName)) {
            return getHelloResponse();
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }/*
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        boolean checkDelivery=true;
        String method="delivery";
        if(intentName.equals("IsOpen")){
        	checkDelivery=false;
            method="both";
        }
        Map<String, Slot> intentSlots=intent.getSlots();
        for(Map.Entry<String, Slot> e:intentSlots.entrySet())
        	log.info("slots", e.getKey()+" : "+e.getValue().getName()+" : "+e.getValue().getValue());
        
        
        
        /*
        Restaurant matchedRestaurant=restaurantSearchService.search(restaurantName,address,defaultRadius,checkDelivery);
        
        
		
	
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
        
		
		*\/
	
			   
			   
		
		
        if ("HelloWorldIntent".equals(intentName)) {
            return getHelloResponse();
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse() {
        String speechText = "Welcome to the Alexa Skills Kit, you can say hello";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("HelloWorld");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelloResponse() {
        String speechText = "Hello world eat now";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("HelloWorld");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText = "You can say hello to me!";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("HelloWorld");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}
