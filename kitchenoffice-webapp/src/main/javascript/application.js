var app = angular.module('kitchenOfficeApp',
		['active.link', '$strap.directives', 'restangular', 'ui.bootstrap', 'ui.map', 'maps.search.input', 'ui.event', 'ui-gravatar' ])
.config([
	'$routeProvider',
	'$locationProvider',
	'RestangularProvider',
	function($routeProvider, $locationProvider, RestangularProvider) {

		$locationProvider.html5Mode(true);

		$routeProvider.when('/kitchenoffice-webapp/home', {
			templateUrl : '/kitchenoffice-webapp/partials/home',
			controller : HomeController
		});

		$routeProvider.when('/kitchenoffice-webapp/event/create', {
			templateUrl : '/kitchenoffice-webapp/partials/event/create',
			controller : EventCreateController
		});

		$routeProvider.otherwise({
			redirectTo : '/kitchenoffice-webapp/home'
		});

		$locationProvider.hashPrefix('!');

	}
]);
app.value('$strapConfig', {
	datepicker : {
		language : 'en',
		format : 'yyyy.mm.dd',
		todayHighlight : true
	}
});
app.run(function($rootScope, $location) {
});

function HomeController($rootScope, $scope, eventService) {
	
	$scope.homeEvents = eventService.getHomeEvents();
	
	$scope.fromNow = function(date) {
		return (date) ? moment(date).calendar() : "not specified";
	};
};

function EventCreateController($rootScope, $scope, eventService) {

	/**
	 * the new event object to be saved
	 */
	$scope.event = {
		type : null,
		date : null,
		location: {}
	};
	
	/**
	 * saves the event to the api
	 */
	$scope.saveEvent = function() {
		// TODO Frontend Validation
		eventService.save($scope.event);
	};

	/**
	 * Time calculations
	 */
	var now = moment();
	var todayStart = now.local().startOf('day');

	// initial values for date and time
	$scope.timeString = todayStart.add(12, 'hours').format('hh:mm A');

	if (moment().hours() > 12) {
		// afternoon so set to tommorrow
		$scope.dateString = todayStart.add(1, 'days').format('YYYY.MM.DD');
	} else {
		// before highnoon
		$scope.dateString = todayStart.format('YYYY.MM.DD');
	}

	// calculating time and update it to the variables
	$scope.dateFromNow = function() {
		var dateDate = moment($scope.dateString);
		var dateTime = moment($scope.timeString, 'hh:mm A');
		var concatenatedDate = moment([ dateDate.years(), dateDate.month(),
				dateDate.date(), dateTime.hours(), dateTime.minutes() ]);

		$scope.event.date = concatenatedDate.format();

		return concatenatedDate.fromNow();
	};

	/**
	 * Google Maps stuff
	 */
	$scope.mapOptions = {
			center : new google.maps.LatLng(35.784, -78.670),
			zoom : 15,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};

	$scope.geolocationAvailable = navigator.geolocation ? true : false;

	if ($scope.geolocationAvailable) {

		navigator.geolocation.getCurrentPosition(function(position) {

			$scope.mapOptions.center = new google.maps.LatLng(
				position.coords.latitude,
				position.coords.longitude
			);

			$scope.$apply();
		}, function() {

		});
	}
}