app.controller('LocationCreateController', function($scope, $rootScope, $location, locationService, flash) {
	
	$scope.location = {};
	
	$scope.isValid = function(locationForm) {
		return locationForm.$valid;
	};
	
	/**
	 * saves the event to the api
	 */
	$scope.saveLocation = function(locationForm) {
		// TODO Frontend Validation
		if($scope.isValid(locationForm)){
			$scope.saveModal.open();
		}
	};
	
	$scope.doSaveLocation = function() {
		
		$rootScope.processing = true;
		
		locationService.save($scope.location).then(function(location) {
			$rootScope.processing = false;
			$scope.doSave = false;
			$window.history.back();
			flash('success', 'New location '+location.name+' saved');
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

	$scope.geolocationAvailable = navigator.geolocation ? true : false;

	if ($scope.geolocationAvailable) {

		navigator.geolocation.getCurrentPosition(function(position) {

			var newCenter = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
			
			$scope.locationMap.setCenter(newCenter);
			$scope.marker.setPosition(newCenter);
		});
	}
	
	/**
	 * location create modal
	 */
	$scope.doSave = false;
	$scope.saveModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doSave = false;
			},
			open: function() {
				$scope.doSave = true;
			}
	};
});