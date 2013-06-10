angular.module('linkModule', []).directive('activeLink', ['$location',
function(location) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs, controller) {
			var clazz = attrs.activeLink;
			var path = attrs.href;
			// hack because path does bot return
			// including hashbang
			path = '/kitchenoffice-webapp/' + path;
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
}]);