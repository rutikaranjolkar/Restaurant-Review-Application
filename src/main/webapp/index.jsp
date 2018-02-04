<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
<script src="js/app.js"></script>
</head>
<body ng-app="webtools">
	<div ng-controller="loginController">
		<div ng-show="sharedData.flagShowLogin">
			Username:
			<input ng-model="username" />
			<br />
			Password:
			<input type="password" ng-model="password" />
			<br />
			Login:
			<button ng-click="submitLogin();" class="btn btn-success">Login</button>
			<br />
			<h3>{{errorMessage}}</h3>
			<a href="#"
				ng-click="sharedData.flagShowLogin = false; sharedData.flagShowRegistration = true;">Register
				here!</a>
		</div>
		<div ng-show="sharedData.flagShowRegistration">
			<h1>Use the form below to register</h1>
			<h4>{{errorMessage}}</h4>
			<table>
				<tr>
					<td>Role:</td>
					<td>
						<select ng-model="newUser.role">
							<option value="CUSTOMER">Customer</option>
							<option value="OWNER">Restaurant Owner</option>
							<option value="ADMIN">Admin</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>Username:</td>
					<td>
						<input ng-model="newUser.username" />
					</td>
				</tr>
				<tr>
					<td>Password:</td>
					<td>
						<input type="password" ng-model="newUser.password" />
					</td>
				</tr>
				<tr>
					<td>First Name:</td>
					<td>
						<input ng-model="newUser.firstName" />
					</td>
				</tr>
				<tr>
					<td>Last Name:</td>
					<td>
						<input ng-model="newUser.lastName" />
					</td>
				</tr>
				<tr>
					<td>Email Address:</td>
					<td>
						<input ng-model="newUser.emailAddress" />
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<button class="btn btn-success" ng-click="register(newUser);">Register</button>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div ng-controller="restaurantController"
		ng-show="sharedData.flagShowMainPage">
		<h2>List of restaurants</h2>
		<button class="btn btn-primary" ng-click="showAddRestaurantPage();"
			ng-show="sharedData.authData.userSession.roleName == 'OWNER' || sharedData.authData.userSession.roleName == 'ADMIN'">Add
			Restaurant</button>
		<button class="btn btn-primary" ng-click="showMyReviews();"
			ng-show="sharedData.authData.userSession.roleName == 'CUSTOMER'">Refresh
			My Reviews</button>
		<button class="btn btn-danger" ng-click="logout();">Logout</button>

		<h2>Welcome {{sharedData.authData.userSession.firstName}}
			{{sharedData.authData.userSession.lastName}}</h2>
		<h2>Role: {{sharedData.authData.userSession.roleName}}</h2>
		Search:
		<input ng-model="searchTerm" />
		<br />
		<table class="table table-striped">
			<tr>
				<th>Name</th>
				<th>Location</th>
				<th>Cuisine</th>
				<th>Description</th>
				<th>Owner</th>
				<th>Action</th>
			</tr>
			<tr ng-repeat="r in restaurants | filter: searchTerm">
				<td>{{r.name}}</td>
				<td>{{r.location}}</td>
				<td>{{r.cuisine}}</td>
				<td>{{r.description}}</td>
				<th>{{r.owner.firstName}} {{r.owner.lastName}}</th>
				<td>
					<button class="btn btn-success" ng-click="showReviews(r);">Show
						Reviews</button>
					<button type="button" value="click"
						ng-click="showAddReviewPage(r);"
						ng-show="sharedData.authData.userSession.roleName == 'CUSTOMER'">Add
						Review</button>
				</td>
			</tr>
		</table>
		<h2>List of reviews for {{selectedRestaurant.name}}</h2>
		<table class="table table-striped">
			<tr>
				<th>Restaurant Name</th>
				<th>Reviewer</th>
				<th>Rating</th>
				<th>Comments</th>
			</tr>
			<tr ng-repeat="rev in reviews">
				<td>{{rev.restaurant.name}}</td>
				<td>{{rev.reviewer.firstName}} {{rev.reviewer.lastName}}</td>
				<th>{{rev.rating}}</th>
				<th>{{rev.comments}}</th>
			</tr>
		</table>

		<div ng-show="sharedData.authData.userSession.roleName=='CUSTOMER'">
			<h2>List of my reviews</h2>
			<table class="table table-striped">
				<tr>
					<th>Restaurant Name</th>
					<th>Rating</th>
					<th>Comments</th>
				</tr>
				<tr ng-repeat="rev in myReviews">
					<td>{{rev.restaurant.name}}</td>
					<th>{{rev.rating}}</th>
					<th>{{rev.comments}}</th>
				</tr>
			</table>
		</div>

		<div ng-show="sharedData.flagShowAddReview">
			<table>
				<tr>
					<td>Rating:</td>
					<td>
						<input ng-model="newReview.rating" type="number" min="1" max="10" />
					</td>
				</tr>
				<tr>
					<td>Comments:</td>
					<td>
						<textarea ng-model="newReview.comments"></textarea>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<button ng-click="submitReview(newReview);"
							class="btn btn-primary">Add review</button>
					</td>
				</tr>
			</table>
		</div>

		<div ng-show="sharedData.flagShowAddRestaurant">
			<h3>Add new Restaurant</h3>
			<table>
				<tr>
					<td>Name:</td>
					<td>
						<input ng-model="newRestaurant.name" />
					</td>
				</tr>
				<tr>
					<td>Location:</td>
					<td>
						<input ng-model="newRestaurant.location" />
					</td>
				</tr>
				<tr>
					<td>Cuisine:</td>
					<td>
						<select ng-model="newRestaurant.cuisine">
							<option value="Chinese">Chinese</option>
							<option value="Indian">Indian</option>
							<option value="American">American</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>Description:</td>
					<td>
						<input ng-model="newRestaurant.description" />
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<button ng-click="submitRestaurant(newRestaurant);"
							class="btn btn-success">Save Restaurant</button>
					</td>
				</tr>
			</table>

		</div>
	</div>


</body>
</html>
