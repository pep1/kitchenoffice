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

function EventCreateController($rootScope, $scope, $location) {
	$scope.event = {};
};