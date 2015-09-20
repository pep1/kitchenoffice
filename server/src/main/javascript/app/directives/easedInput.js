angular.module('ko.directives', []).directive('easedInput', [ '$timeout', function($timeout) {
	return {
		restrict : 'A',
		scope : {
			value : '=',
			timeout : '@',
			placeholder : '@'
		},
		link : function($scope) {
			$scope.timeout = parseInt($scope.timeout, 10);
			$scope.update = function() {
				if ($scope.pendingPromise !== null && typeof $scope.pendingPromise !== "undefined") {
					$timeout.cancel($scope.pendingPromise);
				}
				$scope.pendingPromise = $timeout(function() {
					$scope.value = $scope.currentInputValue;
				}, $scope.timeout);
			};
		}
	};
}]);