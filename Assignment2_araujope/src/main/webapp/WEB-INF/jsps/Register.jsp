<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://www.springframework.org/tags/form" prefix = "form" %>

<!DOCTYPE html>

<!--  
 * Name: Augusto Araujo Peres Goncalez
 * Project: Elections
 * Description: This app allows the user to register and vote in one of 5 different parties. It also display a list
 * 				of all the voters registered, enabling the Edit/Delete of each, and some statistics regarding the data collected.
 * 				The app also allows to add 200 dummy voters and a random number of dummy votes.
 * Date: 31 Mar. 2019
 * 
 * File: Register - Display a form where the user can input their information to register to vote
-->

<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Register to Vote</title>
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
				<a class="nav-item nav-link active" href="/">Home</a>
				<a class="nav-item nav-link" href="#">Register</a>
				<a class="nav-item nav-link" href="vote">Vote</a>
				<a class="nav-item nav-link" href="voters">Voters</a>
				<a class="nav-item nav-link" href="stats">Stats</a>
			</div>
		</div>
	</nav>
	
	<div class="container">
		<!--  Display success or error messages -->
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
		
		<c:url var="url" value="registerVoter" />
		
		<form:form action="${url}" modelAttribute="voter">
			<div class="form-group">
				<label>SIN:</label>
				<form:input type="text" path="sin" name="sin" class="form-control" placeholder="SIN" required="required" />
				<small id="sinHelp" class="form-text text-muted">SIN needs to be a 9 digit number</small>
			</div>
			<div class="form-group">
				<label>First Name:</label>
				<form:input type="text" path="firstName" name="firstName" class="form-control" placeholder="First Name" required="required" />
			</div>
			<div class="form-group">
				<label>Last Name:</label>
				<form:input type="text" path="lastName" name="lastName" class="form-control" placeholder="Last Name" required="required" />
			</div>
			<div class="form-group">
				<label>Birthday:</label>
				<form:input type="date" id="birthday" path="birthday" name="birthday" class="form-control" placeholder="yyyy-mm-dd" required="required" />
				<small id="birthdateHelp" class="form-text text-muted">You need to be older than 18 to vote</small>
			</div>
			<div class="form-group">
				<label>Street:</label>
				<form:input type="text" name="street" path="address.street" class="form-control" placeholder="Street" required="required" />
			</div>
			<div class="form-group">
				<label>City:</label>
				 <form:input type="text" name="city" path="address.city" class="form-control" placeholder="City" required="required" />
			</div>
			<div class="form-group">
				<label>Province:</label>
				 <form:input type="text" name="province" path="address.province" class="form-control" placeholder="Province" required="required" />
			</div>
			<div class="form-group">
				<label>Postal Code:</label>
				 <form:input type="text" name="postal" path="address.postal" class="form-control" placeholder="Postal Code" required="required" />
			</div>
			<input type="submit" value="Register" class="btn btn-default"/>
		</form:form>
	</div>
	
</body>
</html>