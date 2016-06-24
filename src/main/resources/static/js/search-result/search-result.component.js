/* global angular, console */

'use strict';

angular.
        module('searchResult').
        component('searchResult', {
            templateUrl: 'js/search-result/search-result.template.html',
            controller: ['$routeParams', 'File',
                function FileListController($routeParams, File) {
                    var self = this;
                }
            ]
        });

