angular.module('ko.services', [ 'restangular', 'flash' ])
//.factory( 'httpInterceptor', function($rootScope, $q, $window) {
//	
//	// this will be correct for Angularjs version 2.x !!!
//		
//	function request(config) {
//		$rootScope.processing = true;
//	}
//
//	function success(response) {
//		$rootScope.processing = false;
//		return response;
//	}
//
//	function error(response) {
//
//		var status = response.status;
//		var config = response.config;
//		var method = config.method;
//		var url = config.url;
//
//		if (status == 401 || status == 403) {
//			$window.location = $window.location.protocol + "//" + $window.location.host + $window.location.pathname;
//		} else {
//			$rootScope.processing = false;
//			window.scrollTo(0, 0);
//			//flash('error', method + " on " + url + " failed with status " + status + ": " + response.data);
//		}
//
//		return $q.reject(response);
//	}
//
//	return {
//		'request': request,
//		'response': success,
//		'responseError': error
//	};
//})
.config([ 'RestangularProvider', '$httpProvider', function(RestangularProvider, $httpProvider) {
	
	/**
	 * Intercept http errors
	 * */
	var interceptor = function($rootScope, $q, $window, flash) {

		function success(response) {
			$rootScope.processing = false;
			return response;
		}

		function error(response) {

			var status = response.status;
			var config = response.config;
			var method = config.method;
			var url = config.url;

			if (status == 401 || status == 403 || status == 0) {
				$window.location = $window.location.protocol + "//" + $window.location.host + $window.location.pathname;
			} else {
				$rootScope.processing = false;
				// scroll to top in order to see the flash message
				window.scrollTo(0, 0);
				
				if(_.isUndefined(response.data.type) || _.isUndefined(response.data.description)) {
					flash("error", method + " on " + url + " failed with status " + status + ": " + response.data);
				} else {
					flash(response.data.type, response.data.description);
				}
			}

			return $q.reject(response);
		}

		return function(promise) {
			$rootScope.processing = true;
			return promise.then(success, error);
		};
	};
	
	// set http interceptor
	//$httpProvider.interceptors.push('httpInterceptor');
	$httpProvider.responseInterceptors.push(interceptor);
	
	// set base URL
	RestangularProvider.setBaseUrl("/kitchenoffice-webapp/api/v1");
	
}]).factory('eventService', function(Restangular) {
	
	var eventService = Restangular.all('events');

	/**
	 * returns the events for the home view - serves only future events - first
	 * three events
	 */
	eventService.getHomeEvents = function() {
		return this.getList().then(function(events) {
			return _.first( events , 3 );
		});
	};
	
	/**
	 * Attend to an event with an optional job
	 */
	eventService.attendEvent = function(event, job) {
		if(_.isNull(event) || _.isUndefined(event)) return false;
		
		var attendPath = "attend";
		if(!_.isUndefined(job)) {
			attendPath += "/" + job.id;
		}
		return event.customGET(attendPath, function(event) {
			return event;
		});
	};
	
	/**
	 * Attend to an event with an optional job
	 */
	eventService.dismissEvent = function(event, job) {
		if(_.isNull(event) || _.isUndefined(event)) return false;

		return event.customGET("dismiss", function(event) {
			return event;
		});
	};

	/**
	 * saves a given event
	 */
	eventService.save = function(event) {
		if (_.isNull(event) || _.isUndefined(event)) {
			return;
		}
		return this.post(event);
	};
	
	/**
	 * deletes a given event
	 */
	eventService.deleteEvent = function(event) {
		if (_.isNull(event) || _.isUndefined(event)) {
			return;
		}
		return event.remove();
	};

	/**
	 * displays a human readable name of the event
	 */
	eventService.displayName = function(event) {
		switch (event.type) {
		case "EXTERNAL":
			return (event.location.name) ? event.location.name : "";
		case "INTERNAL":
			return (event.recipe.name) ? event.recipe.name : "";
		case "ORDER":
			return (event.location.name) ? event.location.name : "";
		case "FETCH":
			return (event.location.name) ? event.location.name : "";
		default:
			return "";
		}
	};

	return eventService;
}).factory('locationService', function(Restangular) {
	
	var locationService = Restangular.all('locations');
	
	locationService.getLastUsed = function() {
		return this.getList();
	};
	
	locationService.getPages = function(pageSize, search) {
		
		var params = {};
		
		if (search) params.search = search; 
		
		return this.getList(params).then(function(locations) {
			var resultSize = locations.length;
			var pageAmount = Math.ceil(resultSize/pageSize);
			var pointer = 0;
			var output = new Array();
			var rest = resultSize % pageSize;
			
			for ( var i = 0; i < pageAmount; i++) {
				
				if(i < (pageAmount - 1) || (rest == 0)) {
					output.push({
						locations: locations.slice(pointer, pointer + pageSize)
					});
				} else {
					// if we are on the last page, and the rest is not null only take the rest
					output.push({
						locations: locations.slice(pointer, pointer + rest)
					});
				};
				pointer += pageSize;
			};
			return output;
		});
	};
	
	locationService.save = function(location) {
		if (!location) {
			return;
		}
		return this.post(location);
	};
	
	return locationService;
}).factory('userService', function(Restangular) {
	
	var userService = Restangular.all('users');
	
	userService.getUser = function() {
		return this.customGET('me');
	};
	
	userService.getAllUsers = function() {
		return this.getList();
	};
	
	return userService;
});