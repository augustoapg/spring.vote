<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
 * File: Stats - Display basic stats with tables and graphs
-->

<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Stats</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
	
	<style>
		.navbar {
			margin-bottom: 1.5em;
		}
		.table-stats, .chart-stats {
			vertical-align: middle;
		}
		canvas {
			height:400px;
			width: 400px;
		}
		.stats {
			border: 1px solid black;
			display: inline-block;
			margin: 1.5em;
			padding: 1em;
		}
		h1 {
			margin-left: 1em;
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
				<a class="nav-item nav-link" href="vote">Vote</a>
				<a class="nav-item nav-link" href="voters">Voters</a>
				<a class="nav-item nav-link" href="#">Stats</a>
			</div>
		</div>
	</nav>
	<h1>Statistics</h1>
	
	<div class="stats">
		<h3>Voters Stats</h3>
		<div class="table-stats">
			<table class="table">
				<tr>
					<th scope="row">Voters Who Voted</th>
					<td><fmt:formatNumber value="${votersWhoVotedPerc * 100}" maxFractionDigits="2"/>%</td>
				</tr>
				<tr>
					<th scope="row">Voters Who Did Not Vote</th>
					<td><fmt:formatNumber value="${votersWhoDidNotVotePerc * 100}" maxFractionDigits="2"/>%</td>
				</tr>
			</table>
		</div>
		<div class="chart-stats">
			<canvas id="votedPercChart"></canvas>
		</div>
	</div>
	
	<div class="stats">
		<h3>Party Votes Stats</h3>
		<div class="table-stats">
			<table class="table">
				<tr>
					<th scope="row">Liberal</th>
					<td><fmt:formatNumber value="${liberalPerc * 100}" maxFractionDigits="2"/>%</td>
				</tr>
				<tr>
					<th scope="row">Conservative</th>
					<td><fmt:formatNumber value="${conservativePerc * 100}" maxFractionDigits="2"/>%</td>
				</tr>
				<tr>
					<th scope="row">New Democratic</th>
					<td><fmt:formatNumber value="${newDemocraticPerc * 100}" maxFractionDigits="2"/>%</td>
				</tr>
				<tr>
					<th scope="row">Bloc Quebecois</th>
					<td><fmt:formatNumber value="${blocQuebecoisPerc * 100}" maxFractionDigits="2"/>%</td>
				</tr>
				<tr>
					<th scope="row">Green</th>
					<td><fmt:formatNumber value="${greenPerc * 100}" maxFractionDigits="2"/>%</td>
				</tr>
			</table>
		</div>
		<div class="chart-stats">
			<canvas id="partyChart"></canvas>
		</div>
	</div>
	
	<div class="stats">
		<h3>Voters Ages Stats</h3>
		<div class="table-stats">
			<table class="table">
				<tr>
					<th scope="row">18-29</th>
					<td><fmt:formatNumber value="${from18to29Perc * 100}" maxFractionDigits="2"/>%</td>
				</tr>
				<tr>
					<th scope="row">30-44</th>
					<td><fmt:formatNumber value="${from30to44Perc * 100}" maxFractionDigits="2"/>%</td>
				</tr>
				<tr>
					<th scope="row">45-59</th>
					<td><fmt:formatNumber value="${from45to59Perc * 100}" maxFractionDigits="2"/>%</td>
				</tr>
				<tr>
					<th scope="row">60+</th>
					<td><fmt:formatNumber value="${from60Perc * 100}" maxFractionDigits="2"/>%</td>
				</tr>
			</table>
		</div>
		<div class="chart-stats">
			<canvas id="ageChart"></canvas>
		</div>
	</div>
	
	<script>
		$(document).ready(function(){
			var voteCtx = document.getElementById("votedPercChart").getContext('2d');
			var voteChart = new Chart(voteCtx,{
			    type: 'pie',
			    data: {
			    	    datasets: [{
			    	        data: ['${votersWhoVotedPerc}', '${votersWhoDidNotVotePerc}'],
			    	        backgroundColor: [
				                'rgba(0, 255, 0, 0.5)',
				                'rgba(255, 0, 0, 0.5)',
				                ],
			    	    }],
			    	    
			    	    // These labels appear in the legend and in the tooltips when hovering different arcs
			    	    labels: [
			    	        'Voters Who Voted',
			    	        'Voters Who Did Not Vote'
			    	    ]
			    	},
			    options: {
			    	legend: {
			    		position: 'bottom'
			    	}
			    }
			});
			
			var partyCtx = document.getElementById("partyChart").getContext('2d');
			var partyChart = new Chart(partyCtx, {
			    type: 'bar',
			    data: {
			        labels: ["Liberal", "Conservative", "New Democratic", "Bloc Quebecois", "Green"],
			        datasets: [{
			            label: '% of Votes',
			            data: ['${liberalPerc}', '${conservativePerc}', '${newDemocraticPerc}', '${blocQuebecoisPerc}', '${greenPerc}'],
			            backgroundColor: [
			                'rgba(255, 99, 132, 0.2)',
			                'rgba(54, 162, 235, 0.2)',
			                'rgba(255, 206, 86, 0.2)',
			                'rgba(153, 102, 255, 0.2)',
			                'rgba(75, 192, 192, 0.2)'
			            ],
			            borderColor: [
			                'rgba(255,99,132,1)',
			                'rgba(54, 162, 235, 1)',
			                'rgba(255, 206, 86, 1)',
			                'rgba(153, 102, 255, 1)',
			                'rgba(75, 192, 192, 1)'
			            ],
			            borderWidth: 1
			        }]
			    },
			    options: {
			        scales: {
			            yAxes: [{
			                ticks: {
			                    beginAtZero:true
			                }
			            }]
			        },
			        legend: {
			        	display: false
			        }
			    }
			});
			
			var ageCtx = document.getElementById("ageChart").getContext('2d');
			var ageChart = new Chart(ageCtx, {
			    type: 'bar',
			    data: {
			        labels: ["18-29", "30-44", "45-59", "60+"],
			        datasets: [{
			            label: '% of Votes',
			            data: ['${from18to29Perc}', '${from30to44Perc}', '${from45to59Perc}', '${from60Perc}'],
			            backgroundColor: [
			                'rgba(255, 99, 132, 0.2)',
			                'rgba(54, 162, 235, 0.2)',
			                'rgba(255, 206, 86, 0.2)',
			                'rgba(75, 192, 192, 0.2)'
			            ],
			            borderColor: [
			                'rgba(255,99,132,1)',
			                'rgba(54, 162, 235, 1)',
			                'rgba(255, 206, 86, 1)',
			                'rgba(75, 192, 192, 1)'
			            ],
			            borderWidth: 1
			        }]
			    },
			    options: {
			        scales: {
			            yAxes: [{
			                ticks: {
			                    beginAtZero:true
			                }
			            }]
			        },
			        legend: {
			        	display: false
			        }
			    }
			});
		});
	</script>
</body>
</html>