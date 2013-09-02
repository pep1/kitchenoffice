app.controller('LocationSelectController', function($scope, $rootScope, $location, $timeout, locationService, flash) {
	$scope.pages = locationService.getPages(4, '');
	
	$scope.selectedLocation = null;
	$scope.locationSearchString = '';
	$scope.filterText;
	var filterTextTimeout = 300;
	
	$scope.setLocation = function(location) {
		$scope.$parent.event.location = $scope.selectedLocation = location;
	};
	
	$scope.selected = function(location) {
		return ($scope.selectedLocation.id == location.id);
	};
	
	$scope.$watch('locationSearchString', function (val) {
        if (filterTextTimeout) $timeout.cancel(filterTextTimeout);

        tempFilterText = val;
        filterTextTimeout = $timeout(function() {
        	$scope.pages = locationService.getPages(4, (tempFilterText) ? null : tempFilterText);
        });
    });
	
	/*
	 * $scope.selectLocation = function(location) { debugger; if($scope.event) {
	 * //$scope.event.location = location; } };
	 */

});