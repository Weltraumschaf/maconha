'use strict';

angular.
  module('Admin').
  config(['$locationProvider' ,'$routeProvider',
    function config($locationProvider, $routeProvider) {
      $locationProvider.hashPrefix('!');

      $routeProvider.
        when('/file', {
          template: '<file-list></file-list>'
        }).
        when('/keyword', {
          template: '<keyword-list></keyword-list>'
        }).
        when('/media', {
          template: '<media-list></media-list>'
        }).
        when('/job', {
          template: '<media-list></media-list>'
        }).
        when('/overview', {
          template: '<overview></overview>'
        }).
        otherwise('/overview');
    }
  ]);
