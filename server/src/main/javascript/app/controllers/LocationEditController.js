app.controller('LocationEditController', function($scope, $rootScope, $location, $routeParams, $window, locationService, flash) {

	if(isNaN($routeParams.locationId)) {
		$location.path('/kitchenoffice-webapp/home');
		flash('warning', 'Url was not valid: ' + $routeParams.locationId + " cannot be parsed to number");
	}
	
	locationService.getById($routeParams.locationId).then(function(location) {
		$scope.location = location;
		
		var newCenter = new google.maps.LatLng(location.latitude, location.longitude);
		
		$scope.locationMap.setCenter(newCenter);
		if($scope.marker) {
			$scope.marker.setPosition(newCenter);
		}
	});
	
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
			$location.path('/kitchenoffice-webapp/location/' + location.id);
			$rootScope.processing = false;
			$scope.doSave = false;
			flash('success', 'Location '+location.name+' saved');
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