package com.bolt.alexa.skill.Amazonmodel;

import org.springframework.stereotype.Component;

@Component
public class Address {
	private String stateOrRegion;
    private String city;
    private String countryCode;
    private String postalCode;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String districtOrCounty;
    
    public String getTextAdddress() {
    	StringBuilder sb=new StringBuilder();
    	if(addressLine1!=null)
    		sb.append(addressLine1);
    	if(addressLine2!=null)
    		sb.append(", "+addressLine3);
    	if(addressLine3!=null)
    		sb.append(", "+addressLine3);
    	if(city!=null)
    		sb.append(", "+city);
    	if(stateOrRegion!=null)
    		sb.append(", "+stateOrRegion);
    	if(postalCode!=null)
    		sb.append(", "+postalCode);
		return sb.toString();
	}
    @Override
	public String toString() {
		return "Address [stateOrRegion=" + stateOrRegion + ", city=" + city + ", countryCode=" + countryCode
				+ ", postalCode=" + postalCode + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2
				+ ", addressLine3=" + addressLine3 + ", districtOrCounty=" + districtOrCounty + "]";
	}
    private Address() {

    }

    public String getStateOrRegion() {
        return stateOrRegion;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public String getCity() {
        return city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getDistrictOrCounty() {
        return districtOrCounty;
    }
	
}