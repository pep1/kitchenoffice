app.controller('EventDetailsController', function($scope, $rootScope, $location, $routeParams, eventService, flash) {

	if(isNaN($routeParams.eventId)) {
		$location.path('/kitchenoffice-webapp/home');
		flash('warning', 'Url was not valid: ' + $routeParams.eventId + " cannot be parsed to number");
	}
	
	$scope.event = eventService.getEventById($routeParams.eventId);
	
});