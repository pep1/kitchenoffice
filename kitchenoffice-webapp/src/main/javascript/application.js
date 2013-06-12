var app = angular.module('kitchenOfficeApp', ['linkModule', '$strap.directives'])
.config([ '$routeProvider', '$locationProvider', '$httpProvider', 
		function($routeProvider, $locationProvider) {

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

}]);
app.value(
	'$strapConfig', 
	{
		datepicker: {
			language: 'en',
			format: 'yyyy.mm.dd',
			todayHighlight: true
	}
});
app.run(function($rootScope, $location) {

});


function HomeController($rootScope, $scope, $location) {
	
};

function EventCreateController($scope) {
	
	// the new event object
	$scope.event = {
			type: null,
			date: null
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
		var dateDate = moment($scope.dateString)	;
		var dateTime = moment($scope.timeString, 'hh:mm A');
		var concatenatedDate = moment([dateDate.years(), dateDate.month(), dateDate.date(), dateTime.hours(), dateTime.minutes()]);
		
		$scope.event.date = concatenatedDate.format();
		
		return concatenatedDate.fromNow();
	};
	
};