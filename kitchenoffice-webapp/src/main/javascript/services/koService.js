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
	
	locationService.getPages = function(pageSize, search) {
		return this.getList({'search': search}).then(function(locations) {
			var resultSize = locations.length;
			var pageAmount = Math.ceil(resultSize/pageSize);
			var pointer = 0;
			var output = new Array();
			
			for ( var i = 0; i < pageAmount; i++) {
				if(i < (pageAmount - 1)) {
					output.push({
						locations: locations.slice(pointer, pointer + pageSize)
					});
				} else {
					// if we are on the last page, only take the rest
					output.push({
						locations: locations.slice(pointer, pointer + (resultSize % pageSize))
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
});