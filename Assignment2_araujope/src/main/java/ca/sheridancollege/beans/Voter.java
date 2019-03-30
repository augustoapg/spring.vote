package ca.sheridancollege.beans;

import java.util.Calendar;

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
	private Calendar birthday;
	@ManyToOne(cascade = CascadeType.ALL)
	private Address address;
	@OneToOne
	private Vote vote;
}
