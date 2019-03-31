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
		dummyVotersAdded = dao.addDummyVoters();
		model.addAttribute("success_msg", "Dummy voters added!");
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
		
		if(isAgeValid(voter.getBirthday())) {
			if(isSinValid(voter.getSin())) {
				synchronized(Voter.class) {
					dao.registerVoter(voter);
					model.addAttribute("success_msg", "Voter was registered!");
				}
			} else {
				model.addAttribute("error_msg", "SIN must be a 9 digits number");
			}
		} else {
			model.addAttribute("error_msg", "Voter needs to be at least 18 years old");
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
		synchronized(Vote.class) {
			try {
				dao.updateVote(vote.getVoter().getSin(), vote.getPartyVoted());
				model.addAttribute("success_msg", "Thank you for voting!");
			} catch(NotFoundException ex) {
				model.addAttribute("error_msg", ex.getMessage());
			} catch(IllegalArgumentException ex) {
				model.addAttribute("error_msg", ex.getMessage());
			}
		}
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
		// check if sin was changed
		if(!voter.getSin().equals(oldSin)) {
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
					synchronized (Vote.class) {
						try {
							dao.updateVote(voter.getSin(), vote.getPartyVoted());
						} catch(NotFoundException ex) {
							model.addAttribute("error_msg", ex.getMessage());
						} catch(IllegalArgumentException ex) {
							model.addAttribute("error_msg", ex.getMessage());
						}
					}
				}
				
				model.addAttribute("success_msg", "Voter was edited!");
				model.addAttribute("voters", dao.getAllVoters());
				return "Voters";
			} else {
				model.addAttribute("error_msg", "The new SIN value chosen is already registered.");
				model.addAttribute("voter", voter);
				return "EditVoter";
			}
		} else {
			dao.editVoter(voter, oldSin);
			model.addAttribute("success_msg", "Voter was edited!");
			model.addAttribute("voters", dao.getAllVoters());
			return "Voters";
		}
	}
	
	@RequestMapping("/delete/{sin}")
	public String deleteVoter(Model model, @PathVariable String sin) {
		dao.deleteVoterBySin(sin);
		model.addAttribute("success_msg", "Voter was deleted!");
		model.addAttribute("voters", dao.getAllVoters());
		return "Voters";
	}
	
	@RequestMapping("/stats")
	public String goStats() {
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
