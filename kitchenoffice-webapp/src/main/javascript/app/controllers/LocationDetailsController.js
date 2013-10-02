app.controller('LocationDetailsController', function($scope, $rootScope, $location, $routeParams, locationService, flash) {

	if(isNaN($routeParams.locationId)) {
		$location.path('/kitchenoffice-webapp/home');
		flash('warning', 'Url was not valid: ' + $routeParams.locationId + " cannot be parsed to number");
		return;
	}
	
	locationService.getById($routeParams.locationId).then(function(location) {
		$scope.location = location;
		
		var newCenter = new google.maps.LatLng(location.latitude, location.longitude);
		$scope.locationMap.setCenter(newCenter);
		
		new google.maps.Marker({
			map : $scope.locationMap,
			position : $scope.locationMap.center,
			draggable : false
		});
	});
	
	$scope.doSaveLocation = function() {
		
		$rootScope.processing = true;
		
		locationService.save($scope.location).then(function(location) {
			$rootScope.processing = false;
			$scope.doSave = false;
			$location.path('/kitchenoffice-webapp/home');
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

});