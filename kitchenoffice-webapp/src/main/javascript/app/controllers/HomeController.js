app.controller('HomeController', function($rootScope, $scope, eventService, flash) {
	
	$scope.doAttend = false;
	$scope.doDismiss = false;
	$scope.event = null;
	$scope.homeEvents = eventService.getHomeEvents();
	
	$scope.user = $rootScope.me;

	$scope.areEventsEmpty = $scope.homeEvents.then(function(events) {
		return !(events.length > 0);
	});
	
	$scope.containsArrayObject = function(array, obj) {
		return _.contains(array, obj);
	};
	
	$scope.attendEvent = function(event) {
		$rootScope.processing = true;
		eventService.attendEvent(event).then( function(event) {
			window.scrollTo(0, 0);
			$scope.homeEvents = eventService.getHomeEvents();
			flash('success', 'You successfully attend event '+eventService.displayName(event)+'.');
			$scope.attendModal.close();
		}, function() {
			$scope.attendModal.close();
		});
	};
	
	$scope.dismissEvent = function(event) {
		$rootScope.processing = true;
		eventService.dismissEvent(event).then( function(event) {
			window.scrollTo(0, 0);
			$scope.homeEvents = eventService.getHomeEvents();
			flash('success', 'You successfully dismissed event '+eventService.displayName(event)+'.');
			$scope.dismissModal.close();
		}, function() {
			$scope.dismissModal.close();
		});
	};
	
	$scope.attendModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doAttend = false;
				$scope.event = null;
			},
			open: function(event) {
				$scope.doAttend = true;
				$scope.event = event;
			}
	};
	
	$scope.dismissModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doDismiss = false;
				$scope.event = null;
			},
			open: function(event) {
				$scope.doDismiss = true;
				$scope.event = event;
			}
	};
	
});