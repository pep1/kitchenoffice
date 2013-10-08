app.controller('LocationSelectController', function($scope, $rootScope, $location, $timeout, locationService, flash) {
	
	if(!$scope.locationSearchString) {
		$scope.locationSearchString = '';
	}
	
	$scope.reset = function() {
		$scope.pageCount = 0;
		$scope.pageSize = 4;
		$scope.maxPageCount = 2;
		$scope.lastPageFetched = false;
	};
	
	$scope.update = function(searchString) {
		$scope.reset();
		$scope.getPages($scope.pageSize, $scope.pageCount, $scope.maxPageCount, searchString).then(function(pages){
			$scope.pages = pages;
		});
	};
	
	$scope.getPages = function(pageSize, pageCount, maxPages, searchString) {
		return locationService.getPages($scope.pageSize, $scope.pageCount, $scope.maxPageCount, (searchString) ? searchString : null);
	};
	
	$scope.$watch('pages', function(pages) {
		if(!pages || $scope.lastPageFetched) return;
		
		var lastAndActivePage = _.find(pages, function(page) {
			return page.isLast && page.active;
		});
		
		if(lastAndActivePage) {

			$scope.pageCount++;
			// now the user displays the last loaded page
			var morePages = $scope.getPages($scope.pageSize, $scope.pageCount, $scope.maxPageCount, $scope.locationSearchString);
			morePages.then(function(pages) {
				if(!pages || pages.length === 0){
					// seems that we already fetched all pages
					$scope.lastPageFetched = true;
					return;
				}
				// unset the isLast switch on the current last item
				lastAndActivePage.isLast = false;
				// push new loaded values in the pages array
				for ( var i = 0; i < pages.length; i++) {
					$scope.pages.push(pages[i]);
				}
			});
		}
	}, true);
	
	$scope.update();
	
	$scope.selectedLocation = null;
	$scope.filterText = "";
	var filterTextTimeout = null;

	$scope.areLocationsEmpty = function() {
		if(!$scope.pages) return false;
		return ($scope.pages.length <= 0);
	};

	$scope.selectLocation = function(location) {
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