var app = angular.module('kitchenOfficeApp',
		['active.link', '$strap.directives', 'restangular', 'ui.bootstrap', 'ui.map', 'maps.search.input', 'ui.event' ])
.config([
	'$routeProvider',
	'$locationProvider',
	'RestangularProvider',
	function($routeProvider, $locationProvider, RestangularProvider) {
		
		RestangularProvider.setBaseUrl("/kitchenoffice-webapp/api/v1");

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
app.run(function($rootScope, $location, Restangular) {
	$rootScope.events = Restangular.all('events');
});

function HomeController($rootScope, $scope, $location) {
	$scope.homeEvents = $rootScope.events.getList();
};

function EventCreateController($rootScope, $scope) {

	// the new event object
	$scope.event = {
		type : null,
		date : null,
		location: {}
	};

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
	
	$scope.saveEvent = function() {
		// TODO Frontend Validation
		$rootScope.events.post($scope.event);
	}

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

	$scope.searchLocation = function(string, locationMap) {
		
		var map = locationMap;
		var infoWindow = infowindow = new google.maps.InfoWindow();
		var service = new google.maps.places.PlacesService(map);

		var request = {
			location : map.center,
			radius : '1000'
		};

		var callback = function(results, status) {
			if (status == google.maps.places.PlacesServiceStatus.OK) {
				for ( var i = 0; i < results.length; i++) {
					var place = results[i];
					createMarker(place);
				}
			}
		}
		
		function createMarker(place) {
			var placeLoc = place.geometry.location;
			var marker = new google.maps.Marker({
				map : map,
				position : place.geometry.location
			});

			google.maps.event.addListener(marker, 'click', function() {
				infowindow.setContent(place.name);
				infowindow.open(map, this);
			});
		}

		service.search(request, callback);
	};
};