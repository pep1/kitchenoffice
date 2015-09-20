var app = angular.module(
  'kitchenOfficeApp',
  [
    'ko.services',
    'ko.tag.input',
    'ko.textarea',
    'ko.markdown',
    'ko.directives',
    'active.link',
    '$strap.directives',
    'ui.directives',
    'ui.event',
    'ui.bootstrap',
    'ui.bootstrap.carousel',
    'ui.map',
    'maps.search.input',
    'ui-gravatar',
    'flash',
    'infinite-scroll',
    'frapontillo.bootstrap-switch',
  ]).config(
  ['$routeProvider', '$locationProvider', '$httpProvider',
    function ($routeProvider, $locationProvider, $httpProvider) {

      $locationProvider.html5Mode(true);

      $routeProvider.when('/kitchenoffice-webapp/home', {
        templateUrl: '/kitchenoffice-webapp/partials/home',
        controller: 'HomeController'
      });

      $routeProvider.when('/kitchenoffice-webapp/event/create', {
        templateUrl: '/kitchenoffice-webapp/partials/event/create',
        controller: 'EventCreateController'
      });

      $routeProvider.when('/kitchenoffice-webapp/event/:eventId', {
        templateUrl: '/kitchenoffice-webapp/partials/event/details',
        controller: 'EventDetailsController'
      });

      $routeProvider.when('/kitchenoffice-webapp/location', {
        templateUrl: '/kitchenoffice-webapp/partials/location/list',
        controller: 'LocationListController'
      });

      $routeProvider.when('/kitchenoffice-webapp/location/create', {
        templateUrl: '/kitchenoffice-webapp/partials/location/create',
        controller: 'LocationCreateController'
      });

      $routeProvider.when('/kitchenoffice-webapp/location/:locationId', {
        templateUrl: '/kitchenoffice-webapp/partials/location/details',
        controller: 'LocationDetailsController'
      });

      $routeProvider.when('/kitchenoffice-webapp/location/:locationId/edit', {
        templateUrl: '/kitchenoffice-webapp/partials/location/edit',
        controller: 'LocationEditController'
      });

      $routeProvider.otherwise({
        redirectTo: '/kitchenoffice-webapp/home'
      });

      $locationProvider.hashPrefix('!');

    }]);
app.value('$strapConfig', {
  datepicker: {
    language: 'en',
    format: 'yyyy.mm.dd',
    todayHighlight: true
  }
});
app.run(function ($rootScope, $location, locationService, userService, $q) {

  $rootScope.me = userService.getUser();

  $rootScope.refreshUser = function () {
    $rootScope.me = userService.getUser();
  };

  $rootScope.isMe = function (object) {
    if (_.isNull(object) || _.isUndefined(object)) {
      return false;
    }

    // object can be participant or a user object itself
    user = (!_.isUndefined(object.user)) ? object.user : object;

    return $rootScope.me.then(function (me) {
      return (me.id === user.id);
    });
  };

  $rootScope.containsMe = function (array) {

    var deferred = $q.defer();

    if (_.isNull(array) || _.isUndefined(array) || !_.isArray(array) || array.length === 0) {
      return false;
    }

    $rootScope.me.then(function (me) {

      for (var int = 0; int < array.length; int++) {
        var object = array[int];
        var user = (!_.isUndefined(object.user)) ? object.user : object;
        if (me.id === user.id) {
          deferred.resolve(true);
        }
      }

      return deferred.resolve(false);
    });

    return deferred.promise;
  };

  $rootScope.checkBrowserName = function (name) {
    var agent = navigator.userAgent.toLowerCase();
    return (agent.indexOf(name.toLowerCase()) > -1);
  };

  $rootScope.calendar = function (date) {
    return (date) ? moment(date).calendar() : "not specified";
  };

  $rootScope.fromNow = function (date) {
    return (date) ? moment(date).fromNow() : "not specified";
  };

  $rootScope.isEmpty = function (obj) {
    for (var prop in obj) {
      if (obj.hasOwnProperty(prop))
        return false;
    }
    return true;
  };

  $rootScope.processing = false;

  $rootScope.getPaging = function (objects, pageSize, name) {
    var resultSize = objects.length;
    var pageAmount = Math.ceil(resultSize / pageSize);
    var pointer = 0;
    var output = [];
    var rest = resultSize % pageSize;
    var itemsKeyName = (name) ? name : "items";

    for (var i = 0; i < pageAmount; i++) {
      var page = {};
      page.index = i;
      page.isFirst = (i === 0);
      page.isLast = (i === pageAmount - 1);
      if (i < (pageAmount - 1) || (rest === 0)) {
        page[itemsKeyName] = objects.slice(pointer, pointer + pageSize);
      } else {
        // if we are on the last page, and the rest is not null only take the rest
        page[itemsKeyName] = objects.slice(pointer, pointer + rest);
      }

      output.push(page);
      pointer += pageSize;
    }

    return output;
  };
});app.controller('EventCreateController', function($scope, $rootScope, $location, eventService, flash) {
	
	/**
	 * the new event object to be saved
	 */
	$scope.event = {
		type : null,
		startDate : null,
		endDate : null,
		location : null,
		recipe : null,
		validate: function(eventForm) {
			if (!this.type || !this.startDate || !this.endDate) return false;
			var isValid = false;
			
			switch (this.type) {
			case "EXTERNAL":
				isValid =  !_.isNull(this.location);
				break;
			case "FETCH":
				isValid =  !_.isNull(this.location);
				break;
			case "ORDER":
				isValid =  !_.isNull(this.location);
				break;
			case "INTERNAL":
				// TODO
				//isValid =  !_.isNull(this.recipe);
				isValid = true;
				break;
			default:
				break;
			}
			
			return isValid;
		},
		reset: function() {
			this.type = this.location = this.recipe = null;
		}
	};
	
	$scope.isValid = function(eventForm) {
		return $scope.event.validate(eventForm);
	};

	/**
	 * saves the event to the api
	 */
	$scope.saveEvent = function(eventForm) {
		// TODO Frontend Validation
		if($scope.isValid(eventForm)){
			$scope.saveModal.open();
		}
	};
	
	$scope.doSaveEvent = function() {
		
		$rootScope.processing = true;
		
		eventService.save($scope.event).then(function(event) {
			$scope.doSave = false;
			$location.path('/kitchenoffice-webapp/event/' + event.id);
			flash('success', 'New event '+eventService.displayName(event)+' saved');
		}, function(data) {
			$scope.doSave = false;
			$scope.event.reset();
		});
	};

	/**
	 * Modal save stuff
	 */
	$scope.doSave = false;
	$scope.saveModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doSave = false;
			},
			open: function() {
				$scope.doSave = true;
			}
	};

	/**
	 * Time calculations
	 */
	var now = moment();
	var todayStart = now.local().startOf('day');

	// initial values for date and time
	$scope.timeString = todayStart.add(12, 'hours').format('hh:mm A');

	if (moment().hours() > 12) {
		// afternoon so set to tommorrow
		$scope.dateString = todayStart.add(1, 'days').format('YYYY.MM.DD');
	} else {
		// before highnoon
		$scope.dateString = todayStart.format('YYYY.MM.DD');
	}

	// calculating time and update it to the variables
	$scope.dateFromNow = function() {
		var dateDate = moment($scope.dateString);
		// strange behaviour
		if($rootScope.checkBrowserName('firefox')) {
			dateDate = moment($scope.dateString, 'YYYY.MM.DD');
		}
		var dateTime = moment($scope.timeString, 'hh:mm A');
		var concatenatedDate = moment([ dateDate.years(), dateDate.month(), dateDate.date(), dateTime.hours(),
				dateTime.minutes() ]);

		var saveDate = concatenatedDate;
		
		$scope.event.startDate = concatenatedDate.format();
		$scope.event.endDate = concatenatedDate.add('hours', 1).format();
		
		return saveDate.fromNow();
	};
});app.controller('EventDetailsController', function($scope, $rootScope, $location, $routeParams, eventService, flash) {

	if (isNaN($routeParams.eventId)) {
		$location.path('/kitchenoffice-webapp/home');
		flash('warning', 'Url was not valid: ' + $routeParams.eventId + " cannot be parsed to number");
		return;
	}

	$scope.refresh = function() {
		$scope.event = eventService.getById($routeParams.eventId).then(function(event) {
			$scope.eventLocked = event.locked;
			return event;
		});
	};
	$scope.refresh();

	$scope.comment = function(newComment) {
		if (_.isUndefined(newComment) || newComment.length < 4)
			return;
		$scope.event.then(function(event) {
			eventService.commentEvent(event, newComment).then(function(comment) {
				event.comments.push(comment);
				$scope.newComment = "";
			});
		});
	};

	/**
	 * Google Maps stuff
	 */
	$scope.mapOptions = {
		center : new google.maps.LatLng(35.784, -78.670),
		zoom : 15,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};

	$scope.event.then(function(event) {

		var newCenter = new google.maps.LatLng(event.location.latitude, event.location.longitude);
		$scope.locationMap.setCenter(newCenter);

		new google.maps.Marker({
			map : $scope.locationMap,
			position : $scope.locationMap.center,
			draggable : false
		});

	});

	$scope.attendEvent = function(event) {
		$rootScope.processing = true;
		eventService.attendEvent(event).then(function(event) {
			$scope.attendModal.close();
			window.scrollTo(0, 0);
			$scope.refresh();
			flash('success', 'You successfully attend event ' + eventService.displayName(event) + '.');
		}, function() {
			$scope.attendModal.close();
		});
	};

	$scope.dismissEvent = function(event) {
		$rootScope.processing = true;
		eventService.dismissEvent(event).then(function(event) {
			$scope.dismissModal.close();
			window.scrollTo(0, 0);
			$scope.refresh();
			flash('success', 'You successfully dismissed event ' + eventService.displayName(event) + '.');
		}, function() {
			$scope.dismissModal.close();
		});
	};

	$scope.$watch("eventLocked", function(locked) {
		if (typeof locked !== "boolean") {
			return;
		}

		$scope.event.then(function(event) {
			if (locked === event.locked) {
				return;
			}

			if (locked) {
				eventService.lockEvent(event).then(function(updatedEvent) {
					event.locked = updatedEvent.locked;
				});
			} else {
				eventService.unlockEvent(event).then(function(updatedEvent) {
					event.locked = updatedEvent.locked;
				});
			}
		});
	});

	$scope.deleteEvent = function(event) {
		$rootScope.processing = true;
		eventService.deleteEvent(event).then(function(event) {
			$scope.deleteModal.close();
			$location.path('/kitchenoffice-webapp/home');
			window.scrollTo(0, 0);
			flash('success', 'You successfully deleted an event.');
		}, function() {
			$scope.deleteModal.close();
		});
	};

	$scope.doAttend = false;
	$scope.doDismiss = false;
	$scope.doDelete = false;

	$scope.attendModal = {
		opts : {
			backdropFade : true,
			dialogFade : true
		},
		close : function() {
			$scope.doAttend = false;
		},
		open : function(event) {
			$scope.doAttend = true;
		}
	};

	$scope.dismissModal = {
		opts : {
			backdropFade : true,
			dialogFade : true
		},
		close : function() {
			$scope.doDismiss = false;
		},
		open : function(event) {
			$scope.doDismiss = true;
		}
	};

	$scope.deleteModal = {
		opts : {
			backdropFade : true,
			dialogFade : true
		},
		close : function() {
			$scope.doDelete = false;
		},
		open : function(event) {
			$scope.doDelete = true;
		}
	};
});app.controller('HomeController', function($rootScope, $scope, eventService, flash) {
	
	$scope.doAttend = false;
	$scope.doDismiss = false;
	$scope.doDelete = false;
	$scope.event = null;
	
	$scope.pastParams = {
			page: 0,
			limit: 4
	};
	
	$scope.pastEvents = eventService.getPastEvents($scope.pastParams);
	$scope.pastEvents.lastPagereached = false;
	
	$scope.refresh = function() {
		$scope.homeEvents = eventService.getHomeEvents();
	};
	
	$scope.refresh();
	
	$scope.addItems = function() {
		if($scope.lastPageReached) {
			return;
		}
		$scope.pastParams.page++;
		eventService.getPastEvents($scope.pastParams).then(function(moreEvents) {
			if(moreEvents.length < $scope.pastParams.limit) {
				$scope.lastPageReached = true;
				return;
			}
			for ( var i = 0; i < moreEvents.length; i++) {
				$scope.pastEvents.push(moreEvents[i]);
			}
		});
	};

	$scope.areHomeEventsEmpty = $scope.homeEvents.then(function(events) {
		return (events.length <= 0);
	});
	
	$scope.arePastEventsEmpty = $scope.pastEvents.then(function(events) {
		return (events.length <= 0);
	});
	
	$scope.containsArrayObject = function(array, obj) {
		return _.contains(array, obj);
	};
	
	$scope.attendEvent = function(event) {
		$rootScope.processing = true;
		eventService.attendEvent(event).then( function(event) {
			$scope.attendModal.close();
			window.scrollTo(0, 0);
			$scope.refresh();
			flash('success', 'You successfully attend event '+eventService.displayName(event)+'.');
		}, function() {
			$scope.attendModal.close();
		});
	};
	
	$scope.dismissEvent = function(event) {
		$rootScope.processing = true;
		eventService.dismissEvent(event).then( function(event) {
			$scope.dismissModal.close();
			window.scrollTo(0, 0);
			$scope.refresh();
			flash('success', 'You successfully dismissed event '+eventService.displayName(event)+'.');
		}, function() {
			$scope.dismissModal.close();
		});
	};
	
	$scope.deleteEvent = function(event) {
		$rootScope.processing = true;
		eventService.deleteEvent(event).then( function(event) {
			$scope.deleteModal.close();
			window.scrollTo(0, 0);
			$scope.refresh();
			flash('success', 'You successfully deleted event an event.');
		}, function() {
			$scope.deleteModal.close();
		});
	};
	
	$scope.attendModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doAttend = false;
				$scope.event = null;
			},
			open: function(event) {
				$scope.doAttend = true;
				$scope.event = event;
			}
	};
	
	$scope.dismissModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doDismiss = false;
				$scope.event = null;
			},
			open: function(event) {
				$scope.doDismiss = true;
				$scope.event = event;
			}
	};
	
	$scope.deleteModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doDelete = false;
				$scope.event = null;
			},
			open: function(event) {
				$scope.doDelete = true;
				$scope.event = event;
			}
	};
	
});app.controller('LocationCreateController', function($scope, $rootScope, $location, $window, locationService, flash) {
	
	$scope.location = {};
	
	
	$scope.isValid = function(locationForm) {
		return locationForm.$valid;
	};
	
	/**
	 * saves the event to the api
	 */
	$scope.saveLocation = function(locationForm) {
		// TODO Frontend Validation
		if($scope.isValid(locationForm)){
			$scope.saveModal.open();
		}
	};
	
	$scope.doSaveLocation = function() {
		
		$rootScope.processing = true;
		
		locationService.save($scope.location).then(function(location) {
			$location.path('/kitchenoffice-webapp/location/' + location.id);
			$rootScope.processing = false;
			$scope.doSave = false;
			flash('success', 'New location '+location.name+' saved');
		});
	};
	
	/**
	 * Google Maps stuff
	 */
	$scope.mapOptions = {
		center : new google.maps.LatLng(35.784, -78.670),
		zoom : 15,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};

	$scope.geolocationAvailable = navigator.geolocation ? true : false;

	if ($scope.geolocationAvailable) {

		navigator.geolocation.getCurrentPosition(function(position) {

			var newCenter = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
			
			$scope.locationMap.setCenter(newCenter);
			if($scope.marker) {
				$scope.marker.setPosition(newCenter);
			}
		});
	}
	
	/**
	 * location create modal
	 */
	$scope.doSave = false;
	$scope.saveModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doSave = false;
			},
			open: function() {
				$scope.doSave = true;
			}
	};
});app.controller('LocationDetailsController', function($scope, $rootScope, $location, $routeParams, locationService, userService, flash) {

	if(isNaN($routeParams.locationId)) {
		$location.path('/kitchenoffice-webapp/home');
		flash('warning', 'Url was not valid: ' + $routeParams.locationId + " cannot be parsed to number");
		return;
	}
	
	locationService.getById($routeParams.locationId).then(function(location) {
		$scope.location = location;
		$scope.subscribeLocation = location.subscribed;
		
		var newCenter = new google.maps.LatLng(location.latitude, location.longitude);
		$scope.locationMap.setCenter(newCenter);
		
		new google.maps.Marker({
			map : $scope.locationMap,
			position : $scope.locationMap.center,
			draggable : false
		});
	});
	
	$scope.doSaveLocation = function() {
		
		$rootScope.processing = true;
		
		locationService.save($scope.location).then(function(location) {
			$rootScope.processing = false;
			$scope.doSave = false;
			$location.path('/kitchenoffice-webapp/home');
			flash('success', 'New location '+location.name+' saved');
		});
	};
	
	$scope.$watch("subscribeLocation", function(subscribe) {
		if (typeof subscribe !== "boolean" || typeof $scope.location === "undefined") {
			return;
		}
		
		$rootScope.me.then(function(me){
			locationSubscribed = _.some(me.locationSubscriptions, function(locationItem) {
				return $scope.location.id === locationItem.id;
			});
			
			// catch initial setting
			if(locationSubscribed === subscribe) {
				return;
			}
			
			if (subscribe) {
				locationService.subscribe($scope.location).then(function(updatedLocation) {
					$rootScope.refreshUser();
				});
			} else {
				locationService.unsubscribe($scope.location).then(function(updatedLocation) {
					$rootScope.refreshUser();
				});
			}
		});
	});
	
	/**
	 * Google Maps stuff
	 */
	$scope.mapOptions = {
		center : new google.maps.LatLng(35.784, -78.670),
		zoom : 15,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};

});app.controller('LocationEditController', function($scope, $rootScope, $location, $routeParams, $window, locationService, flash) {

	if(isNaN($routeParams.locationId)) {
		$location.path('/kitchenoffice-webapp/home');
		flash('warning', 'Url was not valid: ' + $routeParams.locationId + " cannot be parsed to number");
	}
	
	locationService.getById($routeParams.locationId).then(function(location) {
		$scope.location = location;
		
		var newCenter = new google.maps.LatLng(location.latitude, location.longitude);
		
		$scope.locationMap.setCenter(newCenter);
		if($scope.marker) {
			$scope.marker.setPosition(newCenter);
		}
	});
	
	$scope.isValid = function(locationForm) {
		return locationForm.$valid;
	};
	
	/**
	 * saves the event to the api
	 */
	$scope.saveLocation = function(locationForm) {
		// TODO Frontend Validation
		if($scope.isValid(locationForm)){
			$scope.saveModal.open();
		}
	};
	
	$scope.doSaveLocation = function() {
		
		$rootScope.processing = true;
		
		locationService.save($scope.location).then(function(location) {
			$location.path('/kitchenoffice-webapp/location/' + location.id);
			$rootScope.processing = false;
			$scope.doSave = false;
			flash('success', 'Location '+location.name+' saved');
		});
	};
	
	/**
	 * Google Maps stuff
	 */
	$scope.mapOptions = {
		center : new google.maps.LatLng(35.784, -78.670),
		zoom : 15,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	
	/**
	 * location create modal
	 */
	$scope.doSave = false;
	$scope.saveModal = {
			opts: {
				backdropFade : true,
				dialogFade : true
			},
			close: function() {
				$scope.doSave = false;
			},
			open: function() {
				$scope.doSave = true;
			}
	};
});app.controller('LocationListController', function($scope, $rootScope, $q, $location, $timeout, locationService, flash) {

	if (!$scope.locationSearchString) {
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
		$scope.pages = $scope.getPages($scope.pageSize, $scope.pageCount, $scope.maxPageCount, searchString);
	};

	$scope.getPages = function(pageSize, pageCount, maxPages, searchString) {
		return locationService.getPages($scope.pageSize, $scope.pageCount, $scope.maxPageCount, (searchString) ? searchString : null);
	};

	$scope.$watch('pages', function(pages) {
		if (!pages || $scope.lastPageFetched)
			return;

		var lastAndActivePage = _.find(pages, function(page) {
			return (page.isLast === true && page.active === true);
		});

		// if there is a last and active page and if this page is full load more
		// pages
		if (!_.isUndefined(lastAndActivePage) && (lastAndActivePage.isLast && lastAndActivePage.locations.length === $scope.pageSize)) {

			$scope.pageCount++;
			// now the user displays the last loaded page
			var morePages = $scope.getPages($scope.pageSize, $scope.pageCount, $scope.maxPageCount, $scope.locationSearchString);
			morePages.then(function(pages) {
				if (!pages || pages.length === 0) {
					// seems that we already fetched all pages
					$scope.lastPageFetched = true;
					return;
				}
				// unset the isLast switch on the current last item
				lastAndActivePage.isLast = false;
				// push new loaded values in the pages array
				for (var i = 0; i < pages.length; i++) {
					$scope.pages.$$v.push(pages[i]);
				}
			});
		}
	}, true);

	$scope.update();

	$scope.selectedLocation = null;
	$scope.filterText = "";
	var filterTextTimeout = null;

	$scope.areLocationsEmpty = function() {
		if (_.isNull($scope.pages.$$v) || _.isUndefined($scope.pages.$$v)) {
			return true;
		}
		return ($scope.pages.$$v.length <= 0);
	};

	$scope.selectLocation = function(location) {
		$location.path('/kitchenoffice-webapp/location/' + location.id);
	};

	$scope.selected = function(location) {
		return ($scope.selectedLocation.id == location.id);
	};

	// update data when changing the location search string
	$scope.$watch('locationSearchString', function(val) {
		if (!val) {
			return;
		}

		$timeout.cancel(filterTextTimeout);

		tempFilterText = val;
		filterTextTimeout = $timeout(function() {
			$timeout.cancel(filterTextTimeout);
			$scope.update(tempFilterText);
		}, 200);
	});

	$scope.cleanSearch = function() {
		$scope.locationSearchString = '';
		$scope.update();
	};

});app.controller('LocationSelectController', function($scope, $rootScope, $location, $timeout, locationService, flash) {

	if (!$scope.locationSearchString) {
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
		$scope.pages = $scope.getPages($scope.pageSize, $scope.pageCount, $scope.maxPageCount, searchString);
	};

	$scope.getPages = function(pageSize, pageCount, maxPages, searchString) {
		return locationService.getPages($scope.pageSize, $scope.pageCount, $scope.maxPageCount, (searchString) ? searchString : null);
	};

	$scope.$watch('pages', function(pages) {
		if (!pages || $scope.lastPageFetched) {
			return;
		}

		var lastAndActivePage = _.find(pages, function(page) {
			return (page.isLast === true && page.active === true);
		});

		// if there is a last and active page and if this page is full load more
		// pages
		if (!_.isUndefined(lastAndActivePage) && (lastAndActivePage.isLast && lastAndActivePage.locations.length === $scope.pageSize)) {

			$scope.pageCount++;
			// now the user displays the last loaded page
			var morePages = $scope.getPages($scope.pageSize, $scope.pageCount, $scope.maxPageCount, $scope.locationSearchString);
			morePages.then(function(pages) {
				if (!pages || pages.length === 0) {
					// seems that we already fetched all pages
					$scope.lastPageFetched = true;
					return;
				}
				// unset the isLast switch on the current last item
				lastAndActivePage.isLast = false;
				// push new loaded values in the pages array
				for (var i = 0; i < pages.length; i++) {
					$scope.pages.$$v.push(pages[i]);
				}
			});
		}
	}, true);

	$scope.update();

	$scope.selectedLocation = null;
	$scope.filterText = "";
	var filterTextTimeout = null;

	$scope.areLocationsEmpty = function() {
		if (_.isNull($scope.pages.$$v) || _.isUndefined($scope.pages.$$v)) {
			return true;
		}
		return ($scope.pages.$$v.length <= 0);
	};

	$scope.selectLocation = function(location) {
		$scope.$parent.event.location = $scope.selectedLocation = location;
	};

	$scope.selected = function(location) {
		return ($scope.selectedLocation.id == location.id);
	};

	// update data when changing the location search string
	$scope.$watch('locationSearchString', function(val) {
		if (!val) {
			return;
		}

		$timeout.cancel(filterTextTimeout);

		tempFilterText = val;
		filterTextTimeout = $timeout(function() {
			$timeout.cancel(filterTextTimeout);
			$scope.update(tempFilterText);
		}, 200);
	});

	$scope.cleanSearch = function() {
		$scope.locationSearchString = '';
		$scope.update();
	};

});angular.module('ko.directives', []).directive('easedInput', [ '$timeout', function($timeout) {
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
}]);angular.module('maps.search.input', []).directive('mapsSearch', [ '$location', '$timeout', function(location, $timeout) {
	return {
		restrict : 'A',
		link : function($scope, $element, $attrs) {
			$timeout(function() {

				$scope.isPlace = false;

				var autocomplete = new google.maps.places.Autocomplete($element[0]);

				$scope.marker = new google.maps.Marker({
					map : $scope.locationMap,
					position : $scope.locationMap.center,
					draggable : true
				});

				$scope.infowindow = new google.maps.InfoWindow();

				autocomplete.bindTo('bounds', $scope.locationMap);

				// when the user drags the marker
				google.maps.event.addListener($scope.marker, 'dragend', function() {

					if ($scope.location === null) {
						$scope.location = {};
					}

					$scope.location.latitude = $scope.marker.getPosition().lat();
					$scope.location.longitude = $scope.marker.getPosition().lng();

					// if previously searched a place, truncate all inputs
					if ($scope.isPlace) {
						$scope.isPlace = false;
						$scope.location = null;
					}
				});

				// if the user did some input to the search field
				google.maps.event.addListener(autocomplete, 'place_changed', function() {
					var place = autocomplete.getPlace();

					if (!place.geometry) {
						return;
					}

					if (place.geometry.viewport) {
						$scope.locationMap.fitBounds(place.geometry.viewport);
					} else {
						$scope.locationMap.setCenter(place.geometry.location);
						$scope.locationMap.setZoom(17);
					}

					// if(place.icon) {
					// var image = new google.maps.MarkerImage(
					// place.icon, new google.maps.Size(71, 71),
					// new google.maps.Point(0, 0), new google.maps.Point(17,
					// 34),
					// new google.maps.Size(35, 35));
					// $scope.marker.setIcon(image);
					// }

					$scope.marker.setPosition(place.geometry.location);

					$scope.infowindow.setContent(place.name);
					$scope.infowindow.open($scope.locationMap, $scope.marker);

					$scope.location.name = place.name;
					$scope.location.address = place.formatted_address;
					$scope.location.website = place.website;
					$scope.location.latitude = $scope.marker.getPosition().lat();
					$scope.location.longitude = $scope.marker.getPosition().lng();

					// remember that this was found by google place
					$scope.isPlace = true;
				});

			}, 50);
		}
	};
} ]);angular.module('active.link', []).directive('activeLink', [ '$location', function(location) {

	return {
		restrict : 'A',
		link : function(scope, element, attrs, controller) {
			var clazz = attrs.activeLink;
			var path = attrs.href;

			// hack because path does not return
			// including hashbang
			scope.location = location;

			scope.$watch('location.path()', function(newPath) {
				if (path === newPath) {
					$(element).parent().addClass(clazz);
				} else {
					$(element).parent().removeClass(clazz);
				}
			});
		}
	};
} ]).directive('activeDropdown', [ '$location', function(location) {

	return {
		restrict : 'A',
		link : function(scope, element, attrs, controller) {
			var clazz = attrs.activeDropdown;

			// hack because path does not return
			// including hashbang
			scope.location = location;

			scope.$watch('location.path()', function(newPath) {
				var listItems = $(element).parent().children().find('a[data-active-link]');
				var isActive = false;

				for ( var i = 0; i < listItems.length; i++) {
					if (listItems[i].pathname === newPath) {
						isActive = true;
						break;
					}
				}

				if (isActive) {
					$(element).parent().addClass(clazz);
				} else {
					$(element).parent().removeClass(clazz);
				}
			});
		}
	};
} ]);angular.module('flash', [])
.factory('flash', function($rootScope, $timeout) {
  var messages = [];
  var reset = null;
  
  var cleanup = function() {
    $timeout.cancel(reset);
    reset = $timeout(function() { messages = []; });
  };

  var emit = function() {
    $rootScope.$emit('flash:message', messages, cleanup);
  };

  $rootScope.$on('$routeChangeSuccess', emit);

  var asMessage = function(level, text) {
    if (!text) {
      text = level;
      level = 'success';
    }
    return { level: level, text: text };
  };

  var asArrayOfMessages = function(level, text) {
    if (level instanceof Array) return level.map(function(message) {
      return message.text ? message : asMessage(message);
    }); 
    return text ? [{ level: level, text: text }] : [asMessage(level)];
  };
  
  return function(level, text) {
    emit(messages = asArrayOfMessages(level, text));
  };
})
.directive('flashMessages', function() {
  var directive = { restrict: 'E', replace: true };
  directive.template =
	'<div class="container" ><div ng-repeat="m in messages" class="alert alert-{{m.level}}">' + 
	'<button type="button" class="close" data-dismiss="alert">&times;</button>' + 
	'<div class="pull-left icon-container" ng-switch on="m.level" >' +
	'<i ng-switch-when="success" class="icon-ok icon-2x"></i>' +
	'<i ng-switch-when="error" class="icon-remove icon-2x"></i>' +
	'<i ng-switch-when="warning" class="icon-warning-sign icon-2x"></i>' +
	'<i ng-switch-when="info" class="icon-info icon-2x"></i>' +
	'</div>' +
	'<p>{{m.text}}</p>' + 
	'</div></div>';
  
  directive.controller = function($scope, $rootScope) {
    $rootScope.$on('flash:message', function(_, messages, done) {
      $scope.messages = messages;
      done();
    });
  };

  return directive;
});angular.module('ko.markdown', []).directive('markdown', function() {
	var converter = new Showdown.converter();
	return {
		restrict : 'A',
		replace : true,
		scope : {
			input : "@input",
		},
		link : function(scope, element, attrs) {
			scope.$watch('input', function(val){
				if (!scope.input) return;
				
				var htmlText = converter.makeHtml(scope.input);
				element.html(htmlText);
			});
		}
	};
});angular.module('ko.tag.input', [ 'ko.services', 'ui.bootstrap.typeahead' ]).directive(
		'tagInput',
		function(tagService) {
			return {
				restrict : 'E',
				scope : {
					tags : '='
				},
				template : '<div class="well well-small tags">' + 
					'<span ng-repeat="(idx, tag) in tags" class="badge badge-info tag" >{{tag.name}} <a ng-click="remove(idx)"><i class="icon-remove"></i></a></span>' +
					'</div>' + 
					'<input type="text" placeholder="Add a tag..." ng-model="new_value" data-typeahead="suggestion.name for suggestion in suggestions | filter:$viewValue | limitTo:8" data-typeahead-min-length="2" data-typeahead-wait-ms="100" ></input> ' + 
					'<a class="btn" ng-click="add()">Add</a>',
				link : function($scope, $element, $rootScope) {

					// FIXME: this is lazy and error-prone
					var input = angular.element($element.children()[1]);

					if (_.isUndefined($scope.tags))
						$scope.tags = [];

					// This adds the new tag to the tags array
					$scope.add = function() {

						var newValue = $.trim($scope.new_value);
						var isAlreadyIncluded = (_.find($scope.tags, function(tag) {
							return tag.name === newValue;
						})) ? true : false;

						if (_.isEmpty(newValue) || newValue.length < 3 || isAlreadyIncluded) {
							return false;
						}

						// ensure that we have loaded the suggestions
						var existingTag = _.find($scope.suggestions, function(tag) {
							return tag.name === newValue;
						});

						if (_.isNull(existingTag) || _.isUndefined(existingTag)) {
							// tag with string does not exist yet
							tagService.createTag(newValue).then(function(tag) {
								$scope.tags.push(tag);
								$scope.refresh();
							});
						} else {
							// tag exists already
							$scope.tags.push(existingTag);
						}

						$scope.new_value = "";
					};

					// This is the ng-click handler to remove an item
					$scope.remove = function(idx) {
						$scope.tags.splice(idx, 1);
					};

					$scope.refresh = function() {
						tagService.getAllTags().then(function(tags) {
							$scope.suggestions = tags;
						});
					};

					$scope.refresh();

					// Capture all keypresses
					input.bind('keypress', function(event) {
						// But we only care when Enter was pressed
						if (event.keyCode == 13) {
							// There's probably a better way to handle this...
							$scope.$apply($scope.add);
						}
					});
				}
			};
		});angular.module('ko.textarea', []).directive('textarea', function() {
	return {
		restrict : 'E',
		link : function(scope, element, attributes) {
			var paddingLeft = element.css('paddingLeft'),
				paddingRight = element.css('paddingRight');

			var $shadow = angular.element('<div></div>').css({
				position : 'absolute',
				top : -10000,
				left : -10000,
				width : element[0].offsetWidth - parseInt(paddingLeft || 0) - parseInt(paddingRight || 0),
				fontSize : element.css('fontSize'),
				fontFamily : element.css('fontFamily'),
				lineHeight : element.css('lineHeight'),
				resize : 'none'
			});

			angular.element(document.body).append($shadow);

			var update = function() {
				var times = function(string, number) {
					for (var i = 0, r = ''; i < number; i++) {
						r += string;
					}
					return r;
				};

				var val = element
					.val()
					.replace(/</g, '&lt;')
					.replace(/>/g, '&gt;')
					.replace(/&/g, '&amp;')
					.replace(/\n$/, '<br/>&nbsp;')
					.replace(/\n/g, '<br/>')
					.replace(/\s{2,}/g,
					function(space) {
						return times('&nbsp;', space.length - 1) + ' ';
				});

				$shadow.html(val);

				element.css('height', Math.max($shadow[0].offsetHeight + 10, element[0].offsetHeight));
			};

			scope.$on('$destroy', function() {
				$shadow.remove();
			});

			element.bind('keyup keydown keypress change', update);
			update();
		}
	};
});angular.module('ko.services', [ 'restangular', 'flash' ])
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

} ]).factory('eventService', function($rootScope, Restangular) {

	/**
	 * Prepares all events with additional informations
	 */
	var prepareEvent = function(event) {
		event.canAttend = function() {
			return !$rootScope.containsMe(event.participants);
		};

		event.canDismiss = function() {
			return $rootScope.containsMe(event.participants);
		};
		
		event.participantsContainMe = $rootScope.containsMe(event.participants);
		
		event.hasParticipants = (!_.isNull(event.participants) && !_.isUndefined(event.participants)) ? event.participants.length !== 0 : false;
	};

	var eventService = Restangular.withConfig(function(RestangularConfigurer) {

		RestangularConfigurer.setResponseInterceptor(function(object, operation, what, url, response, deferred) {

			if (operation === 'getList') {
				for (var i = 0; i < object.length; i++) {
					prepareEvent(object[i]);
				}
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
	
	/**
	 * Prepares all locations with additional informations
	 */
	var prepareLocation = function(location) {
		
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
				}
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