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