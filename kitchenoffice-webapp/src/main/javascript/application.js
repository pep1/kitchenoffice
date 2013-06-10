angular.module('kitchenOfficeApp', ['linkModule'])

.config([ '$routeProvider', '$locationProvider', '$httpProvider', 
		function($routeProvider, $locationProvider) {
	
	$locationProvider.html5Mode(true);
	
	$routeProvider.when('/kitchenoffice-webapp/home', {
		templateUrl : '/kitchenoffice-webapp/partials/home',
		controller : EventDisplayController
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


function EventDisplayController($rootScope, $location) {
	
};

function EventCreateController($rootScope, $location) {
	
};