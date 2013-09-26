app.controller('EventDetailsController', function($scope, $rootScope, $location, $routeParams, eventService, flash) {

	if(isNaN($routeParams.eventId)) {
		$location.path('/kitchenoffice-webapp/home');
		flash('warning', 'Url was not valid: ' + $routeParams.eventId + " cannot be parsed to number");
	}
	
	$scope.refresh = function() {
		$scope.event = eventService.getById($routeParams.eventId).then(function(event) {
			event.canAttend = $rootScope.containsMe( event.participants );
			return event;
		});
	};
	$scope.refresh();
	
	$scope.comment = function(newComment) {
		if(_.isUndefined(newComment) || newComment.length < 4) return;
		$scope.event.then(function(event) {
			eventService.commentEvent(event, newComment).then(function(comment) {
				event.comments.push(comment);
				$scope.newComment = "";
			});
		}); 
	};
	
	/**
	 * Google Maps stuff
	 */
	$scope.mapOptions = {
		center : new google.maps.LatLng(35.784, -78.670),
		zoom : 15,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	
	$scope.event.then(function(event) {
		
		var newCenter = new google.maps.LatLng(event.location.latitude, event.location.longitude);
		$scope.locationMap.setCenter(newCenter);
		
		new google.maps.Marker({
			map : $scope.locationMap,
			position : $scope.locationMap.center,
			draggable : false
		});
		
	});
	
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
			$location.path('/kitchenoffice-webapp/home');
			window.scrollTo(0, 0);
			flash('success', 'You successfully deleted an event.');
		}, function() {
			$scope.deleteModal.close();
		});
	};
	
	$scope.doAttend = false;
	$scope.doDismiss = false;
	$scope.doDelete = false;
	
	$scope.attendModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doAttend = false;
			},
			open: function(event) {
				$scope.doAttend = true;
			}
	};
	
	$scope.dismissModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doDismiss = false;
			},
			open: function(event) {
				$scope.doDismiss = true;
			}
	};
	
	$scope.deleteModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doDelete = false;
			},
			open: function(event) {
				$scope.doDelete = true;
			}
	};
});