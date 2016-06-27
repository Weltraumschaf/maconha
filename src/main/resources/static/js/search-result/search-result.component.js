/* global angular, console */

'use strict';

angular.
    module('searchResult').
    component('searchResult', {
        templateUrl: 'js/search-result/search-result.template.html',
        controller: ['$scope', '$rootScope',
            function ($scope, $rootScope) {
                var self = this,
                    rootScope = $rootScope,
                    scope = $scope;

                rootScope.$watch('result', function () {
                    console.log('Update result.');
                    scope.result = rootScope.result;
                });
            }
        ]
    });

