/* global angular, console */

'use strict';

angular.
        module('keywrodList').
        component('keywrodList', {
            templateUrl: 'keywrod-list/keywrod-list.template.html',
            controller: ['$routeParams', 'File',
                function KeywrodListController($routeParams, Keywrod) {
                    var self = this;
                }
            ]
        });

