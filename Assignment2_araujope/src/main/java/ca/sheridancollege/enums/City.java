/**
 * Name: Augusto Araujo Peres Goncalez
 * Project: Elections
 * Description: This app allows the user to register and vote in one of 5 different parties. It also display a list
 * 				of all the voters registered and some statistics regarding the data collected. The app also allows to 
 * 				add 200 dummy voters and a random number of dummy votes.
 * Date: 09 Mar. 2019
 * 
 * File: City - Enum that contains a certain amount of cities to help with randomization of Dummy Voters
 */

package ca.sheridancollege.enums;

public enum City {
	TORONTO ("Toronto", "ON"),
    VANCOUVER   ("Vancouver", "BC"),
    OTTAWA   ("Ottawa", "ON"),
    HALIFAX    ("Halifax", "NS"),
    MONTREAL ("Montreal",   "QC"),
    DARTHMOUTH  ("Darthmouth", "NS"),
    CALGARY  ("Calgary", "AB");

    private final String city;
    private final String province;
    
    City(String city, String province) {
        this.city = city;
        this.province = province;
    }
    
    public String getCity() {
		return city;
	}

	public String getProvince() {
		return province;
	}
}
