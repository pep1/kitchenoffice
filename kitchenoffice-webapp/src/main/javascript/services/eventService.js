app.factory('eventService', function(Restangular) {

	var eventService = Restangular.withConfig(function(RestangularConfigurer) {
		// set base URL
		RestangularConfigurer.setBaseUrl("/kitchenoffice-webapp/api/v1");
		// set client caching to true
		RestangularConfigurer.setDefaultHttpFields({
			cache : true
		});
	}).all('events');

	/**
	 * returns the events for the home view
	 *  - serves only future events
	 */
	eventService.getHomeEvents = function() {
		return this.getList().then(function(events) {
			return _.filter(events, function(event) {
				return moment(event.date).isAfter(moment());
			});
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

	return eventService;
});