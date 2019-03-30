/**
 * Name: Augusto Araujo Peres Goncalez
 * Project: Elections
 * Description: This app allows the user to register and vote in one of 5 different parties. It also display a list
 * 				of all the voters registered and some statistics regarding the data collected. The app also allows to 
 * 				add 200 dummy voters and a random number of dummy votes.
 * Date: 09 Mar. 2019
 * 
 * File: Party - Enum with all available parties to vote. Used to help with the randomization of Dummy Votes
 */

package ca.sheridancollege.enums;

public enum Party {
	LIBERAL ("Liberal", "liberal"),
    CONSERVATIVE   ("Conservative", "conservative"),
    NEW_DEMOCRATIC   ("New Democratic", "newDemocratic"),
    BLOC_QUEBECOIS    ("Bloc Quebecois", "blocQuebecois"),
    GREEN ("Green", "green");

    private final String partyName;
    private final String attrName;
    
    Party(String partyName, String attrName) {
        this.partyName = partyName;
        this.attrName = attrName;
    }
    
    public String getPartyName() {
		return partyName;
	}
    
    public String getAttrName() {
    	return attrName;
    }
}
