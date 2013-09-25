var app = angular.module(
		'kitchenOfficeApp',
		[ 'ko.services', 
		  'ko.tag.input',
		  'ko.textarea',
		  'ko.markdown',
		  'active.link', 
		  '$strap.directives', 
		  'ui.bootstrap', 
		  'ui.bootstrap.carousel',
		  'ui.map', 
		  'maps.search.input', 
		  'ui.event', 
		  'ui-gravatar', 
		  'flash' 
]).config(
[ '$routeProvider', '$locationProvider', '$httpProvider',
  function($routeProvider, $locationProvider, $httpProvider) {

	$locationProvider.html5Mode(true);

	$routeProvider.when('/kitchenoffice-webapp/home', {
		templateUrl : '/kitchenoffice-webapp/partials/home',
		controller : 'HomeController'
	});

	$routeProvider.when('/kitchenoffice-webapp/event/create', {
		templateUrl : '/kitchenoffice-webapp/partials/event/create',
		controller : 'EventCreateController'
	});

	$routeProvider.when('/kitchenoffice-webapp/event/:eventId', {
		templateUrl : '/kitchenoffice-webapp/partials/event/details',
		controller : 'EventDetailsController'
	});
	
	$routeProvider.when('/kitchenoffice-webapp/location', {
		templateUrl : '/kitchenoffice-webapp/partials/location/list',
		controller : 'LocationListController'
	});
	
	$routeProvider.when('/kitchenoffice-webapp/location/create', {
		templateUrl : '/kitchenoffice-webapp/partials/location/create',
		controller : 'LocationCreateController'
	});
	
	$routeProvider.when('/kitchenoffice-webapp/location/:locationId', {
		templateUrl : '/kitchenoffice-webapp/partials/location/details',
		controller : 'LocationDetailsController'
	});
	
	$routeProvider.when('/kitchenoffice-webapp/location/:locationId/edit', {
		templateUrl : '/kitchenoffice-webapp/partials/location/edit',
		controller : 'LocationEditController'
	});

	$routeProvider.otherwise({
		redirectTo : '/kitchenoffice-webapp/home'
	});

	$locationProvider.hashPrefix('!');

} ]);
app.value('$strapConfig', {
	datepicker : {
		language : 'en',
		format : 'yyyy.mm.dd',
		todayHighlight : true
	}
});
app.run(function($rootScope, $location, locationService, userService, $q) {

	$rootScope.me = userService.getUser();

	$rootScope.isMe = function(object) {
		if (_.isNull(object) || _.isUndefined(object)) return false;

		// object can be participant or a user object itself
		user = (!_.isUndefined(object.user)) ? object.user : object;

		return $rootScope.me.then(function(me) {
			return (me.id === user.id);
		});
	};
	
	$rootScope.containsMe = function(array) {
		
		var deferred = $q.defer();
		
		if (_.isNull(array) || _.isUndefined(array) || !_.isArray(array)) deferred.resolve(false);
		
		$rootScope.me.then(function(me) {
			
			for ( var int = 0; int < array.length; int++) {
				var object = array[int];
				user = (!_.isUndefined(object.user)) ? object.user : object;
				if(me.id === user.id) {
					deferred.resolve(true);
				};
			}
			
			return deferred.resolve(false);
		});
		
		return deferred.promise;
	};

	$rootScope.checkBrowserName = function(name) {
		var agent = navigator.userAgent.toLowerCase();
		if (agent.indexOf(name.toLowerCase()) > -1) {
			return true;
		}
	};

	$rootScope.calendar = function(date) {
		return (date) ? moment(date).calendar() : "not specified";
	};
	
	$rootScope.fromNow = function(date) {
		return (date) ? moment(date).fromNow() : "not specified";
	};

	$rootScope.isEmpty = function(obj) {
		for ( var prop in obj) {
			if (obj.hasOwnProperty(prop))
				return false;
		}
		return true;
	};

	$rootScope.processing = false;
	
	$rootScope.getPaging = function(objects, pageSize, name) {
		var resultSize = objects.length;
		var pageAmount = Math.ceil(resultSize/pageSize);
		var pointer = 0;
		var output = new Array();
		var rest = resultSize % pageSize;
		var itemsKeyName = (name) ? name : "items";
		
		for ( var i = 0; i < pageAmount; i++) {
			var page = {};
			page.index = i;
			page.isFirst = (i == 0) ? true : false;
			page.isLast = (i == pageAmount-1) ? true : false;
			if(i < (pageAmount - 1) || (rest == 0)) {
				page[itemsKeyName] = objects.slice(pointer, pointer + pageSize);
			} else {
				// if we are on the last page, and the rest is not null only take the rest
				page[itemsKeyName] = objects.slice(pointer, pointer + rest);
			};
			
			output.push(page);
			pointer += pageSize;
		};
		
		return output;
	};
});