var app = angular.module('webtools', []).run(function($rootScope, $http) {
}).run(function($rootScope) {
	$rootScope.sharedData = {
		flagShowLogin : true,
		flagShowMainPage : false
	};
});

/** LoginController * */
app.controller('loginController', function($scope, $http) {
	$scope.submitLogin = function() {
		$http({
			method : 'POST',
			url : '/finalproject/rest/auth/getToken',
			data : {
				username : $scope.username,
				password : $scope.password
			}
		}).then(function(response) {
			console.log("Got response: ", response);
			var authData = response.data;
			if (authData.success) {
				$scope.sharedData.authData = authData;
				$scope.sharedData.flagShowMainPage = true;
				$scope.sharedData.flagShowLogin = false;
				$scope.username = "";
				$scope.password = "";
				$scope.errorMessage = "";
			} else {
				$scope.errorMessage = "Invalid username/password";
			}
		});
	};

	$scope.register = function(user) {
		$http({
			method : 'POST',
			url : '/finalproject/rest/auth/register',
			data : user
		}).then(function(response) {
			console.log("Got response: ", response);

			var status = response.data;
			if (status.success) {
				$scope.sharedData.flagShowLogin = true;
				$scope.sharedData.flagShowRegistration = false;
				$scope.errorMessage = '';
			} else {
				$scope.errorMessage = status.errorMessage;
			}
		});
	};
});

/** RestaurantController* */
app.controller('restaurantController', function($scope, $http) {
	/** This method is called if the HTTP call succeeds * */
	$http({
		method : 'GET',
		url : '/finalproject/rest/restaurant/getAll'
	}).then(function(response) {
		console.log("Got response: ", response);
		$scope.restaurants = response.data;
	});

	$scope.showReviews = function(restaurant) {
		console.log("Showing reviews for restaurant ID: ", restaurant.id);
		$scope.selectedRestaurant = restaurant;

		$http({
			method : 'POST',
			url : '/finalproject/rest/restaurant/getReviews',
			data : {
				"restaurantId" : restaurant.id
			}
		}).then(function(response) {
			console.log("Got response: ", response);
			$scope.reviews = response.data;
		});

	};

	$scope.showAddReviewPage = function(restaurant) {
		console.log("Adding review for restaurant: ", restaurant);
		$scope.selectedRestaurant = restaurant;
		$scope.sharedData.flagShowAddReview = true;
		$scope.sharedData.flagShowAddRestaurant = false;
	};

	$scope.submitReview = function(review) {
		console.log("Adding review: ", review);
		var restaurant = $scope.selectedRestaurant;
		$http({
			method : 'POST',
			url : '/finalproject/rest/restaurant/addReview',
			headers : {
				'Auth-Token' : $scope.sharedData.authData.authToken
			},
			data : {
				"restaurantId" : restaurant.id,
				"rating" : review.rating,
				"comments" : review.comments
			}
		}).then(function(response) {
			console.log("Got response: ", response);

			$http({
				method : 'POST',
				url : '/finalproject/rest/restaurant/getReviews',
				data : {
					"restaurantId" : restaurant.id
				}
			}).then(function(response) {
				console.log("Got response: ", response);
				$scope.reviews = response.data;
			});

		});

	}

	$scope.showAddRestaurantPage = function() {
		console.log("Showing add new restaurant: ");

		$scope.sharedData.flagShowAddRestaurant = true;
		$scope.sharedData.flagShowAddReview = false;
	};

	$scope.submitRestaurant = function(restaurant) {
		console.log("Adding new restaurant: ", restaurant);

		$http({
			method : 'POST',
			url : '/finalproject/rest/restaurant/add',
			headers : {
				'Auth-Token' : $scope.sharedData.authData.authToken
			},
			data : {
				"name" : restaurant.name,
				"location" : restaurant.location,
				"cuisine" : restaurant.cuisine,
				"description" : restaurant.description
			}
		}).then(function(response) {
			console.log("Got response: ", response);

			$http({
				method : 'GET',
				url : '/finalproject/rest/restaurant/getAll'
			}).then(function(response) {
				console.log("Got response: ", response);
				$scope.restaurants = response.data;
			});
		});

	};

	$scope.logout = function() {
		console.log("Logged out successfully ")
		$scope.sharedData.flagShowMainPage = false;
		$scope.sharedData.flagShowLogin = true;
		$scope.sharedData.flagShowAddReview = false;
		$scope.sharedData.flagShowAddRestaurant = false;
	};

	$scope.showMyReviews = function() {
		$http({
			method : 'POST',
			url : '/finalproject/rest/restaurant/getMyReviews',
			data : {
				username : $scope.sharedData.authData.userSession.username
			}
		}).then(function(response) {
			console.log("Got response: ", response);

			$scope.myReviews = response.data;
		});
	};
});

/** Services * */
