app.controller('EventCreateController', function($scope, $rootScope, $location, eventService, flash) {
	
	/**
	 * the new event object to be saved
	 */
	$scope.event = {
		type : null,
		date : null,
		location : null,
		recipe : null,
		validate: function(eventForm) {
			if (!this.type || !this.date) return false;
			var isValid = false;
			
			if (this.type === 'EXTERNAL') {
				isValid =  !_.isNull(this.location);
			}
			return isValid;
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
			
			$rootScope.processing = false;
			$scope.doSave = false;
			$location.path('/kitchenoffice-webapp/home');
			flash('success', 'New event '+eventService.displayName(event)+' saved');
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
		var dateTime = moment($scope.timeString, 'hh:mm A');
		var concatenatedDate = moment([ dateDate.years(), dateDate.month(), dateDate.date(), dateTime.hours(),
				dateTime.minutes() ]);

		$scope.event.date = concatenatedDate.format();

		return concatenatedDate.fromNow();
	};
});