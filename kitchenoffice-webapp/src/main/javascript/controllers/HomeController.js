app.controller('HomeController', function($rootScope, $scope, eventService) {
	
	$scope.homeEvents = eventService.getHomeEvents();

	$scope.areEventsEmpty = $scope.homeEvents.then(function(events) {
		return !(events.length > 0);
	});
});