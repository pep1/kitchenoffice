angular.module('ko.markdown', [])
/**
 * markdown display
 */
.directive('markdown', function() {
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
})
/**
 * markdown editor with preview
 */
.directive('markdownEditor', [ function() {
	var converter = new Showdown.converter();

	var preview = angular.element('<div class="well well-small"></div>').hide();
	var hint = angular.element('<span class="help-block"> <i class="icon-lightbulb"></i> You can use <a href="http://daringfireball.net/projects/markdown/" target="_blank">Markdown</a>.</span>');
	var iconShow = angular.element('<i class="icon-eye-open"></i>');
	var iconClose = angular.element('<i class="icon-eye-close"></i>');
	var toggleButton = angular.element('<a title="toggle preview" style="cursor: pointer;" class="pull-right" ></a>').html(iconShow);

	var editor = {
		restrict : 'E',
		replace : true,
		transclude : true,
		scope: {
			model : '=model'
		},
		template : '<textarea ng-model="model" ></textarea>',
		link : function(scope, element, attrs) {
			element.after(preview, toggleButton, hint);

			toggleButton.click(function() {
				if(scope.model) {
					if(element.is(":visible")) {
						preview.html(converter.makeHtml(scope.model));
						preview.show();
						element.hide();
						toggleButton.html(iconClose);
					} else {
						preview.hide();
						element.show();
						toggleButton.html(iconShow);
					}
				}
			});
		}
	};

	return editor;
} ]);