app.controller('EventDetailsController', function($scope, $rootScope, $location, $routeParams, eventService, flash) {

	if(isNaN($routeParams.eventId)) {
		$location.path('/kitchenoffice-webapp/home');
		flash('warning', 'Url was not valid: ' + $routeParams.eventId + " cannot be parsed to number");
	}
	
	$scope.event = eventService.getById($routeParams.eventId);
	
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
});