app.controller('LocationSelectController', function($scope, $rootScope, $location, $timeout, locationService, flash) {
	
	if(!$scope.locationSearchString) {
		$scope.locationSearchString = '';
	}
	
	$scope.pages = locationService.getPages(4, null);
	$scope.selectedLocation = null;
	$scope.filterText;
	var filterTextTimeout;

	$scope.areLocationsEmpty = $scope.pages.then(function(pages) {
		return !(pages.length > 0);
	});

	$scope.setLocation = function(location) {
		$scope.$parent.event.location = $scope.selectedLocation = location;
	};

	$scope.selected = function(location) {
		return ($scope.selectedLocation.id == location.id);
	};

	$scope.$watch('locationSearchString', function(val) {
		
		if(!val) return;
		if (filterTextTimeout) $timeout.cancel(filterTextTimeout);

		tempFilterText = val;
		filterTextTimeout = $timeout(function() {
			$scope.doSearch(tempFilterText);
		}, 200);
	});
	
	$scope.doSearch = function(searchString) {
		$scope.pages = locationService.getPages(4, (searchString) ? searchString : null);
	};
	
	$scope.cleanSearch = function() {
		$scope.locationSearchString = '';
		$scope.doSearch();
	};
});