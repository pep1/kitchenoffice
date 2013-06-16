var app = angular.module('kitchenOfficeApp',
		['ko.services', 'active.link', '$strap.directives', 'ui.bootstrap', 'ui.map', 'maps.search.input', 'ui.event', 'ui-gravatar', 'flash' ])
.config([
	'$routeProvider',
	'$locationProvider',
	function($routeProvider, $locationProvider) {

		$locationProvider.html5Mode(true);

		$routeProvider.when('/kitchenoffice-webapp/home', {
			templateUrl : '/kitchenoffice-webapp/partials/home',
			controller : 'HomeController'
		});

		$routeProvider.when('/kitchenoffice-webapp/event/create', {
			templateUrl : '/kitchenoffice-webapp/partials/event/create',
			controller : 'EventCreateController'
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
	
	$rootScope.fromNow = function(date) {
		return (date) ? moment(date).calendar() : "not specified";
	};
	
	$rootScope.isEmpty = function(obj) {
		for ( var prop in obj) {
			if (obj.hasOwnProperty(prop))
				return false;
		}

		return true;
	};
});