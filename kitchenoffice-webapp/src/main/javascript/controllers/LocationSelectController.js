app.controller('LocationSelectController', function($scope, $rootScope, $location, locationService, flash) {

	locationService.getLastUsed();
	
	/*$scope.selectLocation = function(location) {
		debugger;
		if($scope.event) {
			//$scope.event.location = location;
		}
	};*/

});