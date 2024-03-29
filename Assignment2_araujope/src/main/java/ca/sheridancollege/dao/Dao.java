/**
 * Name: Augusto Araujo Peres Goncalez
 * Project: Elections
 * Description: This app allows the user to register and vote in one of 5 different parties. It also display a list
 * 				of all the voters registered, enabling the Edit/Delete of each, and some statistics regarding the data collected.
 * 				The app also allows to add 200 dummy voters and a random number of dummy votes.
 * Date: 31 Mar. 2019
 * 
 * File: Dao - Handles all interactions with the Database
 */

package ca.sheridancollege.dao;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

import com.github.javafaker.Faker;

import ca.sheridancollege.beans.Address;
import ca.sheridancollege.beans.Vote;
import ca.sheridancollege.beans.Voter;
import ca.sheridancollege.enums.Party;
import javassist.NotFoundException;

public class Dao {

	SessionFactory sessionFactory = new Configuration()
			.configure("hibernate.cfg.xml")
			.buildSessionFactory();
	
	/**
	 * Adds 200 dummy voters to the DB. Those voters will take the SIN numbers from
	 * 111222000 to 111222199
	 * 
	 * @return true when successful
	 */
	public boolean addDummyVoters() throws ConstraintViolationException {
		String sin = "";
		String firstName = "";
		String lastName = "";
		Date birthday = null;
		String street = "";
		String city = "";
		String province = "";
		String postal = "";
		Voter voter = null;
		@SuppressWarnings("deprecation")
		Date minBd = new Date(new Date().getYear() - 90, 0, 1);
		@SuppressWarnings("deprecation")
		Date maxBd = new Date(new Date().getYear() - 18, 0, 1);
		Faker faker = new Faker(new Locale("en-CA"));

		for (int i = 0; i < 200; i++) {
			sin = "111222" + String.format("%03d", i);
			firstName = faker.name().firstName();
			lastName = faker.name().lastName();
			birthday = faker.date().between(minBd, maxBd);
			street = faker.address().streetAddress();
			city = faker.address().city();
			province = faker.address().stateAbbr();
			postal = "A" + randomBetween(1, 9) + "B " + randomBetween(1, 9) + "C" + randomBetween(1, 9);
			voter = new Voter(sin, firstName, lastName, birthday, new Address(street, city, province, postal), null);
			
			try {
				registerVoter(voter);
			} catch(IllegalArgumentException ex) {
				throw ex;
			}
			
		}
		
		return true;
	}
	
	/**
	 * Adds a random vote to 60 to 80% of the dummy voters. Returns true if 
	 * successful
	 * 
	 * @throws Exception in case some of the votes where not able to be added
	 */
	public boolean addDummyVotes() throws Exception {
		
		String errorMsg = "";
		
		// take random number of the 200 dummy voters (from 0.6 to 0.8)
		Random r = new Random();
		double randPercent = Math.round((0.6 + (0.8 - 0.6) * r.nextDouble()) * 100.0) / 100.0;
		int numOfDummyVotes = (int) Math.round(200 * randPercent);
		
		for(int i = 0; i < numOfDummyVotes; i++) {
			int sin = 111222000 + i;
			try {
				updateVote(Integer.toString(sin), getRandomParty());
			} catch(IllegalArgumentException ex) {
				errorMsg = "Voter " + sin + ": " + ex.getMessage() + " / ";
			} catch(NotFoundException ex) {
				errorMsg = "Voter " + sin + ": " + ex.getMessage() + " / ";
			}
		}
		
		if(!errorMsg.isEmpty()) {
			throw new Exception(errorMsg.substring(0, errorMsg.length() - 3));
		} else {
			return true;
		}
	}
	
	/**
	 * Gives a random party name contained in the Party enum
	 * @return random party name
	 */
	private String getRandomParty() {
		int index = (int)(Math.random() * ((Party.values().length - 1) + 1));
		return Party.values()[index].getPartyName();
	}
	
	/**
	 * returns a random number between and including a min and a max
	 * 
	 * @param min minimum number in range 
	 * @param max maximum number in range
	 * @return random integer number between range
	 */
	private int randomBetween(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}
	
	public void registerVoter(Voter voter) throws IllegalArgumentException {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		if(getVoterBySin(voter.getSin()) == null) {

			session.save(voter);
			
		} else {
			throw new IllegalArgumentException();
		}
		
		
		
		
		session.getTransaction().commit();
		session.close();
	}
	
	public List<Voter> getAllVoters() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Voter");
		List<Voter> voters = (List<Voter>)query.getResultList();
		
		session.getTransaction().commit();
		session.close();
		
		return voters;
	}
	
	public long getNumberOfVoters() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.createQuery("select count(*) from Voter");
		long numOfVoters = (Long)query.getSingleResult();
		
		session.getTransaction().commit();
		session.close();
		
		return numOfVoters;
	}
	
	public Voter getVoterBySin(String sin) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Voter voter = (Voter)session.get(Voter.class, sin);
		
		session.getTransaction().commit();
		session.close();
		
		return voter;
	}
	
	public Vote getVoteBySin(String sin) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.getNamedQuery("vote.getVoteBySin");
		query.setParameter("sin", sin);
		List<Vote> voteList = (List<Vote>)query.getResultList();
		
		session.getTransaction().commit();
		session.close();
		
		if(voteList.isEmpty()) {
			return null;
		} else {
			return voteList.get(0);
		}
	}
	
	public void updateVote(String sin, String party) throws NotFoundException, IllegalArgumentException {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Voter voter = (Voter)session.get(Voter.class, sin);
		
		if(voter != null) {
			if(voter.getVote() == null) {
				Vote vote = new Vote(party, voter);
				addVote(vote);
				voter.setVote(vote);
			} else {
				session.getTransaction().commit();
				session.close();
				throw new IllegalArgumentException("This voter has already voted. You cannot vote twice.");
			}
		} else {
			session.getTransaction().commit();
			session.close();
			throw new NotFoundException("Voter is not yet registered.");
		}
		
		session.getTransaction().commit();
		session.close();
	}
	
	public void addVote(Vote vote) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(vote);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public long getNumVotesByParty(String party) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.getNamedQuery("vote.numVotesByParty");
		query.setParameter("party", party);
		long numOfVotes = (Long)query.getSingleResult();
		
		session.getTransaction().commit();
		session.close();
		
		return numOfVotes;
	}
	
	public long getNumVotes() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.createQuery("select count(*) from Vote");
		long numOfVotes = (Long)query.getSingleResult();
		
		session.getTransaction().commit();
		session.close();
		
		return numOfVotes;
	}
	
	@SuppressWarnings("deprecation")
	public long getVotersByAgeGroup(int minAge, int maxAge) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		// maxAge and maxBd refer to the older age required
		Date maxBd = new Date();
		maxBd.setYear(maxBd.getYear() - maxAge);
		
		// minAge and minBd refers to the younger age required
		Date minBd = new Date();
		minBd.setYear(minBd.getYear() - minAge);
		
		Query query = session.getNamedQuery("voter.voteByAgeRange");
		query.setParameter("min_date", minBd);
		query.setParameter("max_date", maxBd);
		long numVotesByAge = (Long)query.getSingleResult();
		
		session.getTransaction().commit();
		session.close();
		
		return numVotesByAge;
	}
	
	@SuppressWarnings("deprecation")
	public long getVotersByAgeGroup(int minAge) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Date maxBd = new Date();
		maxBd.setYear(maxBd.getYear() - minAge);
		
		Query query = session.getNamedQuery("voter.ageOlderThan");
		query.setParameter("min_date", maxBd);
		long numVotesByAge = (Long)query.getSingleResult();
		
		session.getTransaction().commit();
		session.close();
		
		return numVotesByAge;
	}
	
	public void editVoter(Voter voter, String sin) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Voter oldVoter = (Voter)session.get(Voter.class, sin);
		
		if(oldVoter != null) {
			oldVoter.setSin(voter.getSin());
			oldVoter.setFirstName(voter.getFirstName());
			oldVoter.setLastName(voter.getLastName());
			oldVoter.setAddress(voter.getAddress());
			oldVoter.setBirthday(voter.getBirthday());
		}
		
		session.getTransaction().commit();
		session.close();
	}

	public void deleteVoterBySin(String sin) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Voter voter = (Voter)session.get(Voter.class, sin);
		if(voter != null) {
			session.delete(voter);
		}
		
		session.getTransaction().commit();
		session.close();
	}
}
