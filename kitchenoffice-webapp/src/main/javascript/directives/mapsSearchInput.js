angular.module('maps.search.input', [])
.directive('mapsSearch', ['$location', '$timeout',
function(location, $timeout) {
	return {
		restrict : 'A',
		link : function($scope, $element, $attrs) {
			$timeout(function(){
				
				$scope.locationMap;
				$scope.isPlace = false;

                var autocomplete = new google.maps.places.Autocomplete($element[0]);
                
                $scope.marker = new google.maps.Marker({
                    map: $scope.locationMap,
                    position: $scope.locationMap.center,
                    draggable: true
                });
                
                $scope.infowindow = new google.maps.InfoWindow();
                
                autocomplete.bindTo('bounds', $scope.locationMap);
                
                // when the user drags the marker
                google.maps.event.addListener($scope.marker, 'dragend', function() {
                	$scope.event.location.latitude = $scope.marker.getPosition().lat();
        			$scope.event.location.longitude = $scope.marker.getPosition().lng();
        			
        			// if previously searched a place, truncate all inputs
        			if($scope.isPlace) {
        				$scope.isPlace = false;
        				$scope.event.location.name = "";
            			$scope.event.location.address = "";
            			$scope.event.location.website = "";
        			};
                });
                
                // if the user did some input to the search field
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
      			  
//      			  if(place.icon) {
//	  				 var image = new google.maps.MarkerImage(
//	  	      			  place.icon, new google.maps.Size(71, 71),
//	  	      			  new google.maps.Point(0, 0), new google.maps.Point(17, 34),
//	  	      			  new google.maps.Size(35, 35));
//	  				 $scope.marker.setIcon(image);
//      			  }
      			  
      			  $scope.marker.setPosition(place.geometry.location);

      			  $scope.infowindow.setContent(place.name);
      			  $scope.infowindow.open($scope.locationMap, $scope.marker);
      			  
      			  $scope.event.location.name = place.name;
      			  $scope.event.location.address = place.formatted_address;
      			  $scope.event.location.website = place.website;
      			  
      			  $scope.event.location.latitude = $scope.marker.getPosition().lat();
      			  $scope.event.location.longitude = $scope.marker.getPosition().lng();
      			  
      			  // remember that this was found by google place
      			  $scope.isPlace = true;
      			});
                
            }, 50);
		}
	};
}]);