app.controller('LocationSelectController', function($scope, $rootScope, $location, locationService, flash) {
	$scope.pages = locationService.getPages(4);
	
	$scope.selectedLocation = null;
	
	$scope.setLocation = function(location) {
		$scope.$parent.event.location = $scope.selectedLocation = location;
	};
	
	$scope.selected = function(location) {
		return ($scope.selectedLocation.id == location.id);
	};
	
	/*
	 * $scope.selectLocation = function(location) { debugger; if($scope.event) {
	 * //$scope.event.location = location; } };
	 */

});