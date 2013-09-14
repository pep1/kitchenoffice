app.controller('LocationSelectController', function($scope, $rootScope, $location, $timeout, locationService, flash) {
	
	if(!$scope.locationSearchString) {
		$scope.locationSearchString = '';
	}
	
	$scope.pageCount = 0;
	$scope.pageSize = 4;
	$scope.maxPageCount = 10;
	
	$scope.update = function(searchString) {
		$scope.pages = $scope.getPages($scope.pageSize, $scope.pageCount, $scope.maxPageCount, searchString);
	};
	
	$scope.getPages = function(pageSize, pageCount, maxPages, searchString) {
		return locationService.getPages($scope.pageSize, $scope.pageCount, $scope.maxPageCount, (searchString) ? searchString : null);
	};
	
	$scope.update();
	
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
			$scope.update(tempFilterText);
			$timeout.cancel(filterTextTimeout);
		}, 200);
	});
	
	$scope.cleanSearch = function() {
		$scope.locationSearchString = '';
		$scope.update();
	};
	
});