app.controller('HomeController', function($rootScope, $scope, eventService, flash) {
	
	$scope.doAttend = false;
	$scope.doDismiss = false;
	$scope.doDelete = false;
	$scope.event = null;
	
	$scope.pastParams = {
			page: 0,
			limit: 3
	};
	
	$scope.pastEvents = eventService.getPastEvents($scope.pastParams);
	
	$scope.refresh = function() {
		$scope.homeEvents = eventService.getHomeEvents();
	};
	
	$scope.refresh();
	
	$scope.addItems = function() {
		$scope.pastParams.page++;
		eventService.getPastEvents($scope.pastParams).then(function(moreEvents) {
			for ( var i = 0; i < moreEvents.length; i++) {
				$scope.pastEvents.push(moreEvents[i]);
			}
		});
	};

	$scope.areHomeEventsEmpty = $scope.homeEvents.then(function(events) {
		return (events.length <= 0);
	});
	
	$scope.arePastEventsEmpty = $scope.pastEvents.then(function(events) {
		return (events.length <= 0);
	});
	
	$scope.containsArrayObject = function(array, obj) {
		return _.contains(array, obj);
	};
	
	$scope.attendEvent = function(event) {
		$rootScope.processing = true;
		eventService.attendEvent(event).then( function(event) {
			$scope.attendModal.close();
			window.scrollTo(0, 0);
			$scope.refresh();
			flash('success', 'You successfully attend event '+eventService.displayName(event)+'.');
		}, function() {
			$scope.attendModal.close();
		});
	};
	
	$scope.dismissEvent = function(event) {
		$rootScope.processing = true;
		eventService.dismissEvent(event).then( function(event) {
			$scope.dismissModal.close();
			window.scrollTo(0, 0);
			$scope.refresh();
			flash('success', 'You successfully dismissed event '+eventService.displayName(event)+'.');
		}, function() {
			$scope.dismissModal.close();
		});
	};
	
	$scope.deleteEvent = function(event) {
		$rootScope.processing = true;
		eventService.deleteEvent(event).then( function(event) {
			$scope.deleteModal.close();
			window.scrollTo(0, 0);
			$scope.refresh();
			flash('success', 'You successfully deleted event an event.');
		}, function() {
			$scope.deleteModal.close();
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
	
	$scope.deleteModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doDelete = false;
				$scope.event = null;
			},
			open: function(event) {
				$scope.doDelete = true;
				$scope.event = event;
			}
	};
	
});