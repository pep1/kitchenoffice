angular.module('ko.markdown', []).directive('markdown', function() {
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
});