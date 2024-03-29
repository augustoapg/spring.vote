/**
 * Name: Augusto Araujo Peres Goncalez
 * Project: Elections
 * Description: This app allows the user to register and vote in one of 5 different parties. It also display a list
 * 				of all the voters registered, enabling the Edit/Delete of each, and some statistics regarding the data collected.
 * 				The app also allows to add 200 dummy voters and a random number of dummy votes.
 * Date: 31 Mar. 2019
 * 
 * File: HomeController - Responsible for all routing. According to each route, it gets the necessary information from Front-end and returns the appropriate
 * 	data back. Also contains logic for adding dummy voters/votes.
 */

package ca.sheridancollege;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.beans.Vote;
import ca.sheridancollege.beans.Voter;
import ca.sheridancollege.dao.Dao;
import ca.sheridancollege.enums.Party;
import javassist.NotFoundException;

@Controller
public class HomeController {
	
	private Dao dao = new Dao();
	private boolean dummyVotersAdded;
	private boolean dummyVotesAdded;

	@RequestMapping("/")
	public String goHome(Model model) {
		// check if dummies are in DB already to enable or disable buttons on front-end
		checkIfDummyVotersAdded();
		checkIfDummyVotesAdded();
		if(dummyVotersAdded) {
			model.addAttribute("dummy_voters_added", true);
			
			if(dummyVotesAdded) {
				model.addAttribute("dummy_votes_added", true);
			}
		}
		return "index";
	}
	
	@RequestMapping("/addDummyVoters")
	public String addDummyVoters(Model model) {
		try {
			dummyVotersAdded = dao.addDummyVoters();
			model.addAttribute("success_msg", "Dummy voters added!");
		} catch(IllegalArgumentException ex) {
			dummyVotersAdded = true;
			model.addAttribute("error_msg", "Dummy voters were already added");
		}
		model.addAttribute("dummy_voters_added", dummyVotersAdded);
		
		return "index";
	}
	
	@RequestMapping("/addDummyVotes")
	public String addDummyVotes(Model model) {
		if(dummyVotersAdded) {
			try {
				dummyVotesAdded = dao.addDummyVotes();
				model.addAttribute("success_msg", "Dummy votes added!");
			} catch(Exception e) {
				model.addAttribute("error_msg", e.getMessage());
			} finally {
				model.addAttribute("dummy_voters_added", dummyVotersAdded);
				model.addAttribute("dummy_votes_added", dummyVotesAdded);
			}
		}
		return "index";
	}
	
	@RequestMapping("/register")
	public String goRegister(Model model, @ModelAttribute Voter voter) {
		model.addAttribute("voter", new Voter());
		return "Register";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@RequestMapping("/registerVoter")
	public String registerVoter(Model model, @ModelAttribute Voter voter, BindingResult result) {
		if(result.hasErrors()) {
			String errMsg = "";
			for(ObjectError err : result.getAllErrors()) {
				errMsg += err.toString() + " ";
			}
			model.addAttribute("error_msg", errMsg);
			return "Register";
		}
		
		model = validateVoter(model, voter);
		if(model.containsAttribute("error_msg")) {
			return "Register";
		}
		synchronized(Voter.class) {
			try {
				dao.registerVoter(voter);
				model.addAttribute("success_msg", "Voter was registered successfully!");
			} catch(IllegalArgumentException ex) {
				model.addAttribute("voter", voter);
				model.addAttribute("error_msg", "A Voter with this SIN is already registered. Please verify your SIN and try again");
				return "Register";
			}
		}
		
		model.addAttribute("voter", new Voter());
		return "Register";
	}
	
	@RequestMapping("/vote")
	public String goVote(Model model) {
		model.addAttribute("vote", new Vote());
		return "Vote";
	}
	
	@RequestMapping("/addVote")
	public String addVote(Model model, @ModelAttribute Vote vote) {
		model = updateVote(model, vote.getVoter().getSin(), vote.getPartyVoted());
		if(model.containsAttribute("error_msg")) {
			model.addAttribute("vote", vote);
			return "Vote";
		}
		model.addAttribute("success_msg", "Thank you for voting!");
		model.addAttribute("vote", new Vote());
		return "Vote";
	}
	
	@RequestMapping("/voters")
	public String goVoters(Model model) {
		List<Voter> allVoters = dao.getAllVoters();
		model.addAttribute("voters", allVoters);
		return "Voters";
	}
	
	@RequestMapping("/edit/{sin}")
	public String goToEditVoter(Model model, @PathVariable String sin) {
		Voter voter = dao.getVoterBySin(sin);
		model.addAttribute("voter", voter);
		return "EditVoter";
	}
	
	@RequestMapping("/editVoter")
	public String editVoter(Model model, @ModelAttribute Voter voter, @RequestParam String oldSin) {
		model = validateVoter(model, voter);
		if(model.containsAttribute("error_msg")) {
			voter.setSin(oldSin);
			model.addAttribute("voter", voter);
			return "EditVoter";
		}
		// check if sin was changed
		if(!voter.getSin().equals(oldSin)) {
			model = editVoterWithSinUpdated(model, voter, oldSin);
			if(model.containsAttribute("error_msg")) {
				voter.setSin(oldSin);
				model.addAttribute("voter", voter);
				return "EditVoter";
			} else {
				model.addAttribute("success_msg", "Voter was edited!");
				model.addAttribute("voters", dao.getAllVoters());
				return "Voters";
			}
		} else {
			dao.editVoter(voter, oldSin);
			model.addAttribute("success_msg", "Voter was edited!");
			model.addAttribute("voters", dao.getAllVoters());
			return "Voters";
		}
	}
	
	private Model editVoterWithSinUpdated(Model model, Voter voter, String oldSin) {
		// check if sin was changed but conflicts with another existing sin
		if(dao.getVoterBySin(voter.getSin()) == null) {
			
			// save vote if existent
			Vote vote = dao.getVoteBySin(oldSin);
			// delete old entry
			dao.deleteVoterBySin(oldSin);
			// register updated voter with new sin
			synchronized(Voter.class) {
				dao.registerVoter(voter);
			}
			//assign old vote to updated entry
			if(vote != null) {
				model = updateVote(model, voter.getSin(), vote.getPartyVoted());
				if(model.containsAttribute("error_msg")) {
					model.addAttribute("voters", dao.getAllVoters());
					return model;
				}
			}
			
			model.addAttribute("success_msg", "Voter was edited!");
			model.addAttribute("voters", dao.getAllVoters());
		} else {
			model.addAttribute("error_msg", "The new SIN value chosen is already registered.");
			model.addAttribute("voter", voter);
		}
		return model;
	}

	private Model updateVote(Model model, String sin, String partyVoted) {
		synchronized (Vote.class) {
			try {
				dao.updateVote(sin, partyVoted);
			} catch(NotFoundException ex) {
				model.addAttribute("error_msg", ex.getMessage());
			} catch(IllegalArgumentException ex) {
				model.addAttribute("error_msg", ex.getMessage());
			}
		}
		return model;
	}

	private Model validateVoter(Model model, Voter voter) {
		if(!isAgeValid(voter.getBirthday())) {
			model.addAttribute("voter", voter);
			model.addAttribute("error_msg", "Voter needs to be at least 18 years old");
		} else if(!isSinValid(voter.getSin())) {
			model.addAttribute("voter", voter);
			model.addAttribute("error_msg", "SIN must be a 9 digits number");
		}
		return model;
	}

	@RequestMapping("/delete/{sin}")
	public String deleteVoter(Model model, @PathVariable String sin) {
		dao.deleteVoterBySin(sin);
		model.addAttribute("success_msg", "Voter was deleted!");
		model.addAttribute("voters", dao.getAllVoters());
		return "Voters";
	}
	
	@RequestMapping("/stats")
	public String goStats(Model model) {
		long numOfVotes = dao.getNumVotes();
		
		// get % of each party
		for(int i = 0; i < Party.values().length; i++) {
			String partyName = Party.values()[i].getPartyName();
			String attrName = Party.values()[i].getAttrName();
			long partyVote = dao.getNumVotesByParty(partyName);
			double percVote = Math.round(((double)partyVote/(double)numOfVotes) * 1000.0) / 1000.0;
			model.addAttribute(attrName + "Perc", percVote);
		}
		
		// get % of eligible voters that did vote
		long numOfVoters = dao.getNumberOfVoters();
		double percVotersWhoVoted = Math.round(((double)numOfVotes/(double)numOfVoters) * 1000.0) / 1000.0;
		double percVotersWhoDidNotVote = 1.0 - percVotersWhoVoted;
		model.addAttribute("votersWhoVotedPerc", percVotersWhoVoted);
		model.addAttribute("votersWhoDidNotVotePerc", percVotersWhoDidNotVote);
		
		// get % of age groups
		long from18to29 = dao.getVotersByAgeGroup(18, 29);
		long from30to44 = dao.getVotersByAgeGroup(30, 44);
		long from45to59 = dao.getVotersByAgeGroup(45, 59);
		long from60 = dao.getVotersByAgeGroup(60);
		
		double from18to29Perc = Math.round(((double)from18to29/(double)numOfVotes) * 1000.0) / 1000.0;
		double from30to44Perc = Math.round(((double)from30to44/(double)numOfVotes) * 1000.0) / 1000.0;
		double from45to59Perc = Math.round(((double)from45to59/(double)numOfVotes) * 1000.0) / 1000.0;
		double from60Perc = Math.round(((double)from60/(double)numOfVotes) * 1000.0) / 1000.0;
		
		model.addAttribute("from18to29Perc", from18to29Perc);
		model.addAttribute("from30to44Perc", from30to44Perc);
		model.addAttribute("from45to59Perc", from45to59Perc);
		model.addAttribute("from60Perc", from60Perc);
		
		return "Stats";
	}
	
	/**
	 * check if dummy voters and votes had been added already or not, updating the 
	 * dummyVotersAdded and dummyVotesAdded variables accordingly
	 * 
	 * return request with updated attribute
	 */
	private boolean checkIfDummyVotersAdded() {
		long numOfVoters = dao.getNumberOfVoters();
		Voter firstDummy = dao.getVoterBySin("111222000");
		
		if(numOfVoters >= 200 && firstDummy != null) {
			dummyVotersAdded = true;
			return true;
		}
		
		return false;
	}
	
	/**
	 * check if dummy votes had been added already or not, updating the 
	 * dummyVotesAdded variable accordingly
	 */
	private void checkIfDummyVotesAdded() {
		if(dummyVotersAdded) {
			long numOfVoters = dao.getNumberOfVoters();
			long numOfVotes = dao.getNumVotes();
			
			if(numOfVotes >= numOfVoters * 0.6) {
				dummyVotesAdded = true;
			}
		}
	}
	
	private boolean isAgeValid(Date bd) {
		Date today = new Date();
		Calendar minBd = Calendar.getInstance();
		minBd.set(Calendar.YEAR, minBd.get(Calendar.YEAR) - 18);

		if (bd.before(minBd.getTime())) {
			return true;
		}
		return false;
	}

	private boolean isSinValid(String sin) {
		if (sin.length() == 9) {

			// checks if all numbers
			try {
				Integer.parseInt(sin);
			} catch (NumberFormatException ex) {
				return false;
			}
			return true;
		}
		return false;
	}
}
