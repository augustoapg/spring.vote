<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>

<!--  
 * Name: Augusto Araujo Peres Goncalez
 * Project: Elections
 * Description: This app allows the user to register and vote in one of 5 different parties. It also display a list
 * 				of all the voters registered, enabling the Edit/Delete of each, and some statistics regarding the data collected.
 * 				The app also allows to add 200 dummy voters and a random number of dummy votes.
 * Date: 31 Mar. 2019
 * 
 * File: index - Display welcome message and allow the user to add Dummy Voters and Votes if those were not yet added
-->

<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Home</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
	<style>
		.navbar {
			margin-bottom: 1.5em;
		}
	</style>
</head>
<body>
	<nav class="navbar navbar-toggleable-md navbar-light bg-faded">
		<button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarNavAltMarkup">
			<div class="navbar-nav">
				<a class="nav-item nav-link active" href="#">Home</a>
				<a class="nav-item nav-link" href="register">Register</a>
				<a class="nav-item nav-link" href="vote">Vote</a>
				<a class="nav-item nav-link" href="voters">Voters</a>
				<a class="nav-item nav-link" href="stats">Stats</a>
			</div>
		</div>
	</nav>
	
	<div class="container">
		<c:if test="${not empty error_msg}">
			<div class="alert alert-warning" role="alert">
				${error_msg}
			</div>
		</c:if>
		<c:if test="${not empty success_msg}">
			<div class="alert alert-success" role="alert">
				${success_msg}
			</div>
		</c:if>
		
		<h2>Welcome to Election App</h2>
		<p>Use the menu above to navigate our app</p>
		
		<c:if test="${empty dummy_voters_added}">
			<p>If you wish to add Dummy Voters, click the button below</p>
		</c:if>
		
		<c:if test="${not empty dummy_voters_added and empty dummy_votes_added}">
			<p>If you wish to add Dummy Votes for the Dummy Voters, click the button below</p>
		</c:if>
		
		
		<form method="post" action="addDummyVoters">
			<c:if test="${empty dummy_voters_added}">
				<input type="submit" value="Add Dummy Voters" class="btn btn-default">
			</c:if>
		</form>
		
		<form method="post" action="addDummyVotes">
			<c:if test="${not empty dummy_voters_added and empty dummy_votes_added}">
				<input type="submit" value="Add Dummy Votes" class="btn btn-default">
			</c:if>
		</form>
	</div>
</body>
</html>