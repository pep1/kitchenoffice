angular.module('flash', [])
.factory('flash', function($rootScope, $timeout) {
  var messages = [];
  var reset = null;
  
  var cleanup = function() {
    $timeout.cancel(reset);
    reset = $timeout(function() { messages = []; });
  };

  var emit = function() {
    $rootScope.$emit('flash:message', messages, cleanup);
  };

  $rootScope.$on('$routeChangeSuccess', emit);

  var asMessage = function(level, text) {
    if (!text) {
      text = level;
      level = 'success';
    }
    return { level: level, text: text };
  };

  var asArrayOfMessages = function(level, text) {
    if (level instanceof Array) return level.map(function(message) {
      return message.text ? message : asMessage(message);
    }); 
    return text ? [{ level: level, text: text }] : [asMessage(level)];
  };
  
  return function(level, text) {
    emit(messages = asArrayOfMessages(level, text));
  };
})

.directive('flashMessages', function() {
  var directive = { restrict: 'E', replace: true };
  directive.template =
	  '<div class="container" ><div ng-repeat="m in messages" class="alert alert-{{m.level}}">' + 
	   '<button type="button" class="close" data-dismiss="alert">&times;</button>' + 
	   '<div class="pull-left icon-container" ng-switch on="m.level" >' +
	   '<i ng-switch-when="success" class="icon-ok icon-2x"></i>' +
	   '<i ng-switch-when="error" class="icon-remove icon-2x"></i>' +
	   '<i ng-switch-when="warning" class="icon-warning-sign icon-2x"></i>' +
	   '<i ng-switch-when="info" class="icon-info icon-2x"></i>' +
	   '</div>' +
	   '<p>{{m.text}}</p>' + 
	   '</div></div>';
  
  
  directive.controller = function($scope, $rootScope) {
    $rootScope.$on('flash:message', function(_, messages, done) {
      $scope.messages = messages;
      done();
    });
  };

  return directive;
});