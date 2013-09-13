app.controller('HomeController', function($rootScope, $scope, eventService) {
	
	$scope.homeEvents = eventService.getHomeEvents();

	$scope.areEventsEmpty = $scope.homeEvents.then(function(events) {
		return !(events.length > 0);
	});
	
	$scope.attendEvent = function(event) {
		eventService.attendEvent(event);
	};
	
	$scope.doAttend = false;
	$scope.selectedEvent = null;
	$scope.attendModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doAttend = false;
				$scope.selectedEvent = null;
			},
			open: function(event) {
				$scope.doAttend = true;
				$scope.selectedEvent = event;
			}
	};
	
	$scope.doAttendEvent = function(event) {
		
	};
});