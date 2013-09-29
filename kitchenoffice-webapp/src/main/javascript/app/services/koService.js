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
				//window.scrollTo(0, 0);
				
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
	
}]).factory('eventService', function($rootScope, Restangular) {
	
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
	 * returns the past events for the home view - serves only past events
	 */
	eventService.getPastEvents = function() {
		return this.customGETLIST('past');
	};
	
	/**
	 * returns the event with specified id
	 */
	eventService.getById = function(id) {
		if(isNaN(id)) return {};
		return Restangular.one("events", id).get();
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
	 * Dismiss an event
	 */
	eventService.dismissEvent = function(event) {
		if(_.isNull(event) || _.isUndefined(event)) return false;

		return event.customGET("dismiss", function(event) {
			return event;
		});
	};
	
	/**
	 * Comment an event
	 */
	eventService.commentEvent = function(event, string) {
		if(_.isNull(event) || _.isUndefined(event)) return {};
		if(_.isNull(string) || _.isUndefined(string)) return event;

		var comment = {
				"text": string
		};
		
		return event.post("comment", comment);
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
}).factory('locationService', function($rootScope, Restangular) {
	
	var locationService = Restangular.all('locations');
	
	locationService.getLastUsed = function() {
		return this.getList();
	};
	
	/**
	 * returns the location with specified id
	 */
	locationService.getById = function(id) {
		if(isNaN(id)) return {};
		return Restangular.one("locations", id).get();
	};
	
	locationService.getPages = function(pageSize, page, maxPageFetchCount, search) {

		if(_.isUndefined(maxPageFetchCount)) maxPageFetchCount = 2;
		var params = {
				page: page,
				size: pageSize * maxPageFetchCount
		};
		
		if (search) params.search = search; 
		
		return this.getList(params).then(function(locations) {
			return $rootScope.getPaging(locations, pageSize, "locations");
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
}).factory('tagService', function(Restangular) {
	
	var tagService = Restangular.all('tags');
	
	tagService.getAllTags = function() {
		return this.getList();
	};
	
	tagService.createTag = function(tagName) {
		if(_.isEmpty(tagName)) return false;
		
		var tag = {
			name: tagName
		};
		
		return this.post(tag);
	};
	
	return tagService;
});