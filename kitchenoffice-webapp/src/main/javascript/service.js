var services = angular.module('kitchenoffice.services', ['restangular']);

services.config(function(RestangularProvider) {
  RestangularProvider.setBaseUrl("/kitchenoffice/api/v1");
});