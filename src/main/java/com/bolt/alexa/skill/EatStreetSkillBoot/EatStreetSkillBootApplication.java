package com.bolt.alexa.skill.EatStreetSkillBoot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

@SpringBootApplication
@ComponentScan(basePackages="com.bolt.alexa.skill")
public class EatStreetSkillBootApplication extends SpringBootServletInitializer{
	/*
	@Autowired
    private HelloWorldSpeechlet mySpeechlet;

    @Bean
    public ServletRegistrationBean registerServlet() {

        SpeechletServlet speechletServlet = new SpeechletServlet();
        speechletServlet.setSpeechlet(mySpeechlet);

        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(speechletServlet, "/hello");
        return servletRegistrationBean;     
    }*/
    @Autowired
    private EatStreetSpeechlet eatStreetSpeechlet;

    @Bean
    public ServletRegistrationBean registerServlet() {

        SpeechletServlet speechletServlet = new SpeechletServlet();
        speechletServlet.setSpeechlet(eatStreetSpeechlet);

        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(speechletServlet, "/eat");
        return servletRegistrationBean;     
    }
    
    
	public static void main(String[] args) {
		SpringApplication.run(EatStreetSkillBootApplication.class, args);
	}
}
