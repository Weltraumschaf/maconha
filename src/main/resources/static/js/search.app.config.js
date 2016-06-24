'use strict';

angular.
  module('Search').
  config(['$locationProvider',
    function config($locationProvider) {
      $locationProvider.hashPrefix('!');
    }
  ]);
