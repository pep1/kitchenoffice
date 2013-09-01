angular.module('ko.services', [ 'restangular' ])
.config([ 'RestangularProvider', function(RestangularProvider) {
	// set base URL
	RestangularProvider.setBaseUrl("/kitchenoffice-webapp/api/v1");
	// set client caching to true
	RestangularProvider.setDefaultHttpFields({
		cache : true
	});
} ]).factory('eventService', function(Restangular) {
	
	var eventService = Restangular.withConfig(function(RestangularConfigurer) {
		RestangularConfigurer.setDefaultHttpFields({
			cache : true
		});
		
//		RestangularConfigurer.addElementTransformer('events', true, function(one, two, three) {
//			
//		});
//		
//		RestangularConfigurer.addElementTransformer('events', false, function(one, two, three) {
//			debugger;
//		});
	}).all('events');

	/**
	 * returns the events for the home view - serves only future events - first
	 * three events
	 */
	eventService.getHomeEvents = function() {
		return this.getList().then(function(events) {
			return _.first(_.filter(events, function(event) {
				return moment(event.date).isAfter(moment());
			}), 3);
		});
	};

	/**
	 * saves a given event
	 */
	eventService.save = function(event) {
		if (!event) {
			return;
		}
		return this.post(event);
	};

	eventService.displayName = function(event) {
		switch (event.type) {
		case "EXTERNAL":
			return (event.location.name) ? event.location.name : "";
		case "INTERNAL":
			return (event.recipe.name) ? event.recipe.name : "";
		case "ORDER":
			return (event.location.name) ? event.location.name : "";
		default:
			return "";
		}
	};

	return eventService;
}).factory('locationService', function(Restangular) {
	
	var locationService = Restangular.withConfig(function(RestangularConfigurer) {
		RestangularConfigurer.setDefaultHttpFields({
			cache : true
		});
		
	}).all('locations');
	
	locationService.getLastUsed = function() {
		return this.getList();
	};
	
	locationService.save = function(location) {
		if (!location) {
			return;
		}
		return this.post(location);
	};
	
	return locationService;
});