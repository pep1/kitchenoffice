app.controller('HomeController', function($rootScope, $scope, eventService, flash) {
	
	$scope.homeEvents = eventService.getHomeEvents();

	$scope.areEventsEmpty = $scope.homeEvents.then(function(events) {
		return !(events.length > 0);
	});
	
	$scope.attendEvent = function(event) {
		eventService.attendEvent(event).then( function(event) {
			window.scrollTo(0, 0);
			$rootScope.processing = false;
			$scope.attendModal.close();
			$scope.homeEvents = eventService.getHomeEvents();
			flash('success', 'You successfully attend event '+eventService.displayName(event)+'.');
		}, function(data) {
			window.scrollTo(0, 0);
			$rootScope.processing = false;
			$scope.attendModal.close();
			$scope.homeEvents = eventService.getHomeEvents();
			flash('error', data[0].data.description);
		});
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