/**
 * Name: Augusto Araujo Peres Goncalez
 * Project: Elections
 * Description: This app allows the user to register and vote in one of 5 different parties. It also display a list
 * 				of all the voters registered, enabling the Edit/Delete of each, and some statistics regarding the data collected.
 * 				The app also allows to add 200 dummy voters and a random number of dummy votes.
 * Date: 31 Mar. 2019
 * 
 * File: Voter - Java Bean for creating a Voter object
 */

package ca.sheridancollege.beans;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@NamedQuery(name="voter.voteByAgeRange", query = "select count(*) from Voter where vote_voteId is not null and birthday between :max_date and :min_date")
@NamedQuery(name="voter.ageOlderThan", query = "select count(*) from Voter where vote_voteId is not null and birthday <= :min_date")
public class Voter {
	@Id
	private String sin;
	private String firstName;
	private String lastName;
	@Temporal(TemporalType.DATE)
	private Date birthday;
	@ManyToOne(cascade = CascadeType.ALL)
	private Address address;
	@OneToOne(cascade= {CascadeType.REMOVE})
	private Vote vote;
}
