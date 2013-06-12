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
	
	// calculate actual now rounded to quarter of an hour
	var now = new Date();
	var mins = now.getMinutes();
	var quarterHours = Math.round(mins/15);
	if (quarterHours == 4) {
	    now.setHours(now.getHours()+1);
	}
	now.setMinutes((quarterHours*15)%60);
	
	
	// initial values for date and time
	$scope.timeString = moment(now).local().format('hh:mm A');
	$scope.dateString = moment().local().startOf('day').format('YYYY.MM.DD');
	
	
	// calculating time and update it to the variables
	$scope.dateFromNow = function() {
		var dateDate = moment($scope.dateString)	;
		var dateTime = moment($scope.timeString, 'hh:mm A');
		var concatenatedDate = moment([dateDate.years(), dateDate.month(), dateDate.date(), dateTime.hours(), dateTime.minutes()]);
		
		$scope.event.date = concatenatedDate.format();
		
		return concatenatedDate.fromNow();
	};
	
};