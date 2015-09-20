angular.module('ko.tag.input', [ 'ko.services', 'ui.bootstrap.typeahead' ]).directive(
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
		});