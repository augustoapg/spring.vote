/**
 * Name: Augusto Araujo Peres Goncalez
 * Project: Elections
 * Description: This app allows the user to register and vote in one of 5 different parties. It also display a list
 * 				of all the voters registered, enabling the Edit/Delete of each, and some statistics regarding the data collected.
 * 				The app also allows to add 200 dummy voters and a random number of dummy votes.
 * Date: 31 Mar. 2019
 * 
 * File: Vote - Java Bean for creating a Vote object
 */

package ca.sheridancollege.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@NamedQuery(name = "vote.numVotesByParty", query = "select count(*) from Vote where partyVoted = :party")
@NamedQuery(name = "vote.getVoteBySin", query = "from Vote where voter_sin = :sin")
public class Vote {

	@Id
	@GeneratedValue
	private int voteId;
	private String partyVoted;
	@OneToOne
	private Voter voter;
	
	@Transient
	private String[] possibleParties = {"Liberal", "Conservative", "New Democratic", "Bloc Quebecois", "Green"};
	
	public Vote(String partyVoted, Voter voter) {
		this.partyVoted = partyVoted;
		this.voter = voter;
	}
}