/* global angular, console */

'use strict';

angular.
        module('mediaList').
        component('mediaList', {
            templateUrl: 'media-list/media-list.template.html',
            controller: ['$routeParams', 'Media',
                function MediaListController($routeParams, Media) {
                    var self = this;
                }
            ]
        });


