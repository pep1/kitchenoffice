angular.module('kitchenOfficeApp', ['linkModule', '$strap.directives'])

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

}]).run(function($rootScope, $location) {

});


function HomeController($rootScope, $scope, $location) {
	
};

function EventCreateController($scope) {
	
	// the new event object
	$scope.event = {
			type: null,
			date: null
	};
	
	// calculate actual now rounded to quarter of an hour
	var now = new Date();
	var mins = now.getMinutes();
	var quarterHours = Math.round(mins/15);
	if (quarterHours == 4) {
	    now.setHours(now.getHours()+1);
	}
	now.setMinutes((quarterHours*15)%60);
	
	// initial values for date and time
	$scope.timeString = moment(now).format('HH:mm a');
	$scope.dateString;
	
	// calculating the real date value
	$scope.date = function() {
		var dateDate = moment($scope.dateString);
		var dateTime = moment($scope.timeString, 'HH:mm a');
		var duration = moment.duration({ 'hours': dateTime.hours(), 'minutes': dateTime.minutes()});
		
		dateDate.add(duration);
		
		$scope.event.date = dateDate;
		
		return dateDate;
	};
};