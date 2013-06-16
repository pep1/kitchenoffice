angular.module('maps.search.input', [])
.directive('mapsSearch', ['$location', '$timeout',
function(location, $timeout) {
	return {
		restrict : 'A',
		link : function($scope, $element, $attrs) {
			$timeout(function(){
				
				$scope.locationMap;

                var autocomplete = new google.maps.places.Autocomplete($element[0]);
                
                $scope.marker = new google.maps.Marker({
                    map: $scope.locationMap
                  });
                
                $scope.infowindow = new google.maps.InfoWindow();
                
                autocomplete.bindTo('bounds', $scope.locationMap);
                
                google.maps.event.addListener(autocomplete, 'place_changed', function() {
      			  var place = autocomplete.getPlace();
      			  
      			  if(!place.geometry) {
      				  return;
      			  }
      			  
      			  if (place.geometry.viewport) {
      				$scope.locationMap.fitBounds(place.geometry.viewport);
      			  } else {
      				$scope.locationMap.setCenter(place.geometry.location);
      				$scope.locationMap.setZoom(17);
      			  }
      			  var image = new google.maps.MarkerImage(
      			      place.icon, new google.maps.Size(71, 71),
      			      new google.maps.Point(0, 0), new google.maps.Point(17, 34),
      			      new google.maps.Size(35, 35));
      			  $scope.marker.setIcon(image);
      			  $scope.marker.setPosition(place.geometry.location);

      			  $scope.infowindow.setContent(place.name);
      			  $scope.infowindow.open($scope.locationMap, $scope.marker);
      			  
      			  $scope.event.location.name = place.name;
      			  $scope.event.location.address = place.formatted_address;
      			  $scope.event.location.website = place.website;
      			  
      			  $scope.event.location.latitude = place.geometry.location.lat();
      			  $scope.event.location.longitude = place.geometry.location.lng();
      			});
                
            }, 50);
		}
	};
}]);