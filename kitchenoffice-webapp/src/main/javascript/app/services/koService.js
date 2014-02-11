angular.module('ko.services', [ 'restangular', 'flash' ])
// .factory( 'httpInterceptor', function($rootScope, $q, $window) {
//	
// // this will be correct for Angularjs version 2.x !!!
//		
// function request(config) {
// $rootScope.processing = true;
// }
//
// function success(response) {
// $rootScope.processing = false;
// return response;
// }
//
// function error(response) {
//
// var status = response.status;
// var config = response.config;
// var method = config.method;
// var url = config.url;
//
// if (status == 401 || status == 403) {
// $window.location = $window.location.protocol + "//" + $window.location.host +
// $window.location.pathname;
// } else {
// $rootScope.processing = false;
// window.scrollTo(0, 0);
// //flash('error', method + " on " + url + " failed with status " + status + ":
// " + response.data);
// }
//
// return $q.reject(response);
// }
//
// return {
// 'request': request,
// 'response': success,
// 'responseError': error
// };
// })
.config([ 'RestangularProvider', '$httpProvider', function(RestangularProvider, $httpProvider) {

	/**
	 * Intercept http errors
	 */
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

			if (status === 401 || status === 403 || status === 0) {
				$window.location = $window.location.protocol + "//" + $window.location.host + $window.location.pathname;
			} else {
				$rootScope.processing = false;
				// scroll to top in order to see the flash message
				// window.scrollTo(0, 0);

				if (_.isUndefined(response.data.type) || _.isUndefined(response.data.description)) {
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
	// $httpProvider.interceptors.push('httpInterceptor');
	$httpProvider.responseInterceptors.push(interceptor);

	// set base URL
	RestangularProvider.setBaseUrl("/kitchenoffice-webapp/api/v1");

} ])
/**
 * Event Service
 */
.factory('eventService', function($rootScope, Restangular) {

	/**
	 * Prepares all events with additional informations
	 */
	var prepareEvent = function(event) {
		
		event.getThumbURL = function(width, height) {
			if(_.isUndefined(this.image) || _.isNull(this.image)) {
				return null;
			}
			
			if(!width) {
				return null;
			}
			
			var baseName = this.image.fileName.substr(0, this.image.fileName.lastIndexOf('.')); 
			
			if(height) {
				return $rootScope.thumbBasePath + baseName + "-" + width + "x" + height + ".jpg";
			} else {
				
				var ratio = this.image.width / this.image.height;
				var scaleRatio;

				if (ratio > 1) {
					// Width larger than Height
					scaleRatio = width / this.image.height;
				} else {
					scaleRatio = width / this.image.width;
				}

				var newWidth = Math.ceil((this.image.width * scaleRatio));
				var newHeight = Math.ceil((this.image.height * scaleRatio));
				
				return $rootScope.thumbBasePath + baseName + "-" + newWidth + "x" + newHeight + ".jpg";
			};
		};
		
		event.participantsContainMe = $rootScope.containsMe(event.participants);
		event.hasParticipants = (!_.isNull(event.participants) && !_.isUndefined(event.participants)) ? event.participants.length !== 0 : false;
	};

	var eventService = Restangular.withConfig(function(RestangularConfigurer) {

		RestangularConfigurer.setResponseInterceptor(function(object, operation, what, url, response, deferred) {

			if (operation === 'getList') {
				for (var i = 0; i < object.length; i++) {
					prepareEvent(object[i]);
				};
			} else {
				prepareEvent(object);
			}

			return object;
		});
	}).all('events');

	/**
	 * returns the events for the home view - serves only future events - first
	 * three events
	 */
	eventService.getHomeEvents = function() {
		return this.getList().then(function(events) {
			return _.first(events, 3);
		});
	};

	/**
	 * returns the past events for the home view - serves only past events
	 */
	eventService.getPastEvents = function(params) {
		return this.customGETLIST('past', params);
	};

	/**
	 * returns the event with specified id
	 */
	eventService.getById = function(id) {
		if (isNaN(id)) {
			return {};
		}
		return this.one(id).get();
	};

	/**
	 * Attend to an event with an optional job
	 */
	eventService.attendEvent = function(event, job) {
		if (_.isNull(event) || _.isUndefined(event))
			return false;

		var attendPath = "attend";
		if (!_.isUndefined(job)) {
			attendPath += "/" + job.id;
		}
		return this.one(event.id, attendPath).get();
	};

	/**
	 * Dismiss an event
	 */
	eventService.dismissEvent = function(event) {
		if (_.isNull(event) || _.isUndefined(event))
			return false;

		return this.one(event.id, "dismiss").get();
	};

	/**
	 * Comment an event
	 */
	eventService.commentEvent = function(event, string) {
		if (_.isNull(event) || _.isUndefined(event))
			return {};
		if (_.isNull(string) || _.isUndefined(string))
			return event;

		var comment = {
			"text" : string
		};

		return this.one(event.id).post("comment", comment);
	};

	eventService.lockEvent = function(event) {
		if (_.isNull(event) || _.isUndefined(event) || typeof event.id !== 'number') {
			return;
		}

		if (!$rootScope.isMe(event.creator)) {
			flash("warning", "You are not allowed to lock this event");
			return;
		}

		return this.one(event.id, "lock").get();
	};

	eventService.unlockEvent = function(event) {
		if (_.isNull(event) || _.isUndefined(event) || typeof event.id !== 'number') {
			return;
		}

		if (!$rootScope.isMe(event.creator)) {
			flash("warning", "You are not allowed to unlock this event");
			return;
		}

		return this.one(event.id, "unlock").get();
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
		
		return this.one(event.id).remove();
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
})
/**
 * Location Service
 */
.factory('locationService', function($rootScope, Restangular) {
	
	/**
	 * Prepares all locations with additional informations
	 */
	var prepareLocation = function(location) {
		
		/**
		 * creates an url for the thumb servlet
		 */
		location.getThumbURL = function(width, height) {
			if(_.isUndefined(this.image) || _.isNull(this.image)) {
				return null;
			}
			
			if(!width) {
				return null;
			}
			
			var baseName = this.image.fileName.substr(0, this.image.fileName.lastIndexOf('.')); 
			
			if(height) {
				return $rootScope.thumbBasePath + baseName + "-" + width + "x" + height + ".jpg";
			} else {
				
				var ratio = this.image.width / this.image.height;
				var scaleRatio;

				if (ratio > 1) {
					// Width larger than Height
					scaleRatio = width / this.image.height;
				} else {
					scaleRatio = width / this.image.width;
				}

				var newWidth = Math.ceil((this.image.width * scaleRatio));
				var newHeight = Math.ceil((this.image.height * scaleRatio));
				
				return $rootScope.thumbBasePath + baseName + "-" + newWidth + "x" + newHeight + ".jpg";
			};
		};
		
		/**
		 * check if the user has subscribed to this location
		 */
		location.subscribed = $rootScope.me.then(function(me) {
				return _.some(me.locationSubscriptions, function(locationItem) {
					return location.id === locationItem.id;
				});
			});
	};

	var locationService = Restangular.withConfig(function(RestangularConfigurer) {
		RestangularConfigurer.setResponseInterceptor(function(object, operation, what, url, response, deferred) {

			if (operation === 'getList') {
				for (var i = 0; i < object.length; i++) {
					prepareLocation(object[i]);
				};
			} else {
				prepareLocation(object);
			}

			return object;
		});
	}).all('locations');

	locationService.getLastUsed = function() {
		return this.getList();
	};

	/**
	 * returns the location with specified id
	 */
	locationService.getById = function(id) {
		if (isNaN(id)) {
			return {};
		}
		return this.one(id).get();
	};

	locationService.getPages = function(pageSize, page, maxPageFetchCount, search) {

		if (_.isUndefined(maxPageFetchCount)){
			maxPageFetchCount = 2;
		}
		
		var params = {
			page : page,
			limit : pageSize * maxPageFetchCount
		};

		if (search)
			params.search = search;

		return this.getList(params).then(function(locations) {
			return $rootScope.getPaging(locations, pageSize, "locations");
		});
	};
	
	locationService.subscribe = function(location) {
		if(_.isNull(location) || _.isUndefined(location) || typeof location.id !== 'number') {
			return {};
		}
		
		return this.one(location.id, "subscribe").get();
	};
	
	locationService.unsubscribe = function(location) {
		if(_.isNull(location) || _.isUndefined(location) || typeof location.id !== 'number') {
			return {};
		}
		
		return this.one(location.id, "unsubscribe").get();
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
		if (_.isEmpty(tagName))
			return false;

		var tag = {
			name : tagName
		};

		return this.post(tag);
	};

	return tagService;
});