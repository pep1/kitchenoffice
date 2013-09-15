angular.module('active.link', []).directive('activeLink', ['$location',
function(location) {
	
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
					scope.recursiveDo($(element).parent(), function(element) {
						element.addClass(clazz);
					});
				} else {
					scope.recursiveDo($(element).parent(), function(element) {
						element.removeClass(clazz);
					});
				}
			});
			
			scope.recursiveDo = function(element, callback) {
				callback($(element));
				
				if($(element).parent().parent().hasClass("dropdown")) {
					scope.recursiveDo($(element).parent().parent(), callback);
				}
			};
			
		}
	};
}]);