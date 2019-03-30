<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://www.springframework.org/tags/form" prefix = "form" %>

<!DOCTYPE html>

<!--  
 * Name: Augusto Araujo Peres Goncalez
 * Project: Elections
 * Description: This app allows the user to register and vote in one of 5 different parties. It also display a list
 * 				of all the voters registered and some statistics regarding the data collected. The app also allows to 
 * 				add 200 dummy voters and a random number of dummy votes.
 * Date: 09 Mar. 2019
 * 
 * File: Vote - Display a form where the user can vote for one of 5 parties
-->

<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Vote</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
	<style>
		.not-selected-msg {
			color: red;
		}
		.hidden {
			display: none;
		}
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
				<a class="nav-item nav-link active" href="/">Home</a>
				<a class="nav-item nav-link" href="register">Register</a>
				<a class="nav-item nav-link" href="#">Vote</a>
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
		
		<c:url var="url" value="/addVote" />
		
		<form:form id="frmVote" action="${url}" modelAttribute="vote">
			<div class="form-group">
				<label>SIN:</label>
				<form:input type="text" name="sin" path="voter.sin" class="form-control" placeholder="SIN" required="required" />
				<div class="input-group mb-3">
				  	<form:select class="form-control" name="party-voted" id="ddl-party" path="partyVoted" items="${vote.possibleParties}" />
				</div>
				<p class="not-selected-msg hidden">Please select one of the parties</p>
				<input id="btnSubmit" type="submit" value="Vote!" class="btn btn-default"/>
			</div>
		</form:form>
	</div>
	
	<script>
		$(document).ready(function(){
			
			$('#frmVote').on('submit', function(e){
				if($("#ddl-party").val() !== "not-selected") {
					$(".not-selected-msg").addClass("hidden");
					$("#frmVote").submit();
				} else {
					$(".not-selected-msg").removeClass("hidden");
					e.preventDefault();
				}
			});
			
			$('#ddl-party').change(function() {
				$('.not-selected-msg').addClass('hidden');
			});
		});
	</script>
</body>
</html>