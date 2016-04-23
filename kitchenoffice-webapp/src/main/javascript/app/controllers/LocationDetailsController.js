app.controller('LocationDetailsController', function($scope, $rootScope, $location, $routeParams, locationService, userService, flash) {

	if(isNaN($routeParams.locationId)) {
		$location.path('/kitchenoffice-webapp/home');
		flash('warning', 'Url was not valid: ' + $routeParams.locationId + " cannot be parsed to number");
		return;
	}
	
	locationService.getById($routeParams.locationId).then(function(location) {
		$scope.location = location;
		$scope.subscribeLocation = location.subscribed;
		
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
	
	$scope.$watch("subscribeLocation", function(subscribe) {
		if (typeof subscribe !== "boolean" || typeof $scope.location === "undefined") {
			return;
		}
		
		$rootScope.me.then(function(me){
			var locationSubscribed = _.some(me.locationSubscriptions, function(locationItem) {
				return $scope.location.id === locationItem.id;
			});
			
			// catch initial setting
			if(locationSubscribed === subscribe) {
				return;
			}
			
			if (subscribe) {
				locationService.subscribe($scope.location).then(function(updatedLocation) {
					$rootScope.refreshUser();
				});
			} else {
				locationService.unsubscribe($scope.location).then(function(updatedLocation) {
					$rootScope.refreshUser();
				});
			}
		});
	});
	
	/**
	 * Google Maps stuff
	 */
	$scope.mapOptions = {
		center : new google.maps.LatLng(35.784, -78.670),
		zoom : 15,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};

});