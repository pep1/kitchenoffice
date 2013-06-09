angular.module('kitchenOfficeApp', [])
.config([ '$routeProvider', '$locationProvider', '$httpProvider', 
		function($routeProvider, $locationProvider) {
	
	$routeProvider.when('/home', {
		templateUrl : 'partials/home',
		controller : EventDisplayController
	});
	
	$routeProvider.when('/event/create', {
		templateUrl : 'partials/event/create',
		controller : EventCreateController
	});
	
	$routeProvider.otherwise({
		redirectTo : '/home'
	});
	
	$locationProvider.hashPrefix('!');

}]).run(function($rootScope, $location) {

});


function EventDisplayController($rootScope, $location) {
	
};

function EventCreateController($rootScope, $location) {
	
};