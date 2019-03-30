package ca.sheridancollege.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@NamedQuery(name = "vote.numVotesByParty", query = "select count(*) from Vote where partyVoted = :party")
public class Vote {

	@Id
	@GeneratedValue
	private int voteId;
	private String partyVoted;
	@OneToOne
	private Voter voter;
	
	public Vote(String partyVoted, Voter voter) {
		this.partyVoted = partyVoted;
		this.voter = voter;
	}
}