<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>

<!--  
 * Name: Augusto Araujo Peres Goncalez
 * Project: Elections
 * Description: This app allows the user to register and vote in one of 5 different parties. It also display a list
 * 				of all the voters registered, enabling the Edit/Delete of each, and some statistics regarding the data collected.
 * 				The app also allows to add 200 dummy voters and a random number of dummy votes.
 * Date: 31 Mar. 2019
 * 
 * File: Voters - Display a table with all the voters currently registered
-->
 
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Voters</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
	<style>
		.navbar {
			margin-bottom: 1.5em;
		}
		.voted {
			color: green;
		}
		.not-voted {
			color: red;
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
				<a class="nav-item nav-link" href="/register">Register</a>
				<a class="nav-item nav-link" href="/vote">Vote</a>
				<a class="nav-item nav-link" href="#">Voters</a>
				<a class="nav-item nav-link" href="/stats">Stats</a>
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
		<h1>Voters Registered</h1>
		<span>There are currently ${fn:length(voters)} voters registered</span>
	
		<table class="table table-hover">
			<thead>
			    <tr>
			    	<th scope="col">SIN</th>
			    	<th scope="col">Full Name</th>
			    	<th scope="col">Birthday</th>
			    	<th scope="col">Address</th>
			    	<th scope="col">Voted?</th>
			    	<th></th>
			    	<th></th>
			    </tr>
			</thead>
			<tbody>
				<c:forEach var="voter" items="${voters}">
					<tr>
						<td>${voter.sin}</td>
						<td>${voter.firstName} ${voter.lastName}</td>
						<td>${voter.birthday }</td>
						<td>${voter.address.street}, ${voter.address.city} - ${voter.address.province} (${voter.address.postal})</td>
						<c:if test="${not empty voter.vote.voteId}"><td class="voted">Yes</td></c:if>
						<c:if test="${empty voter.vote.voteId}"><td class="not-voted">No</td></c:if>
						<td><a href="<c:url value="/edit/${voter.sin}/" />">Edit</a></td>
						<td><a href="<c:url value="/delete/${voter.sin}/" />">Delete</a></td>
					</tr>
				</c:forEach>
			</tbody>
			
		</table>
		
	</div>
</body>
</html>