angular.module('active.link', []).directive('activeLink', [ '$location', function(location) {

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
} ]);