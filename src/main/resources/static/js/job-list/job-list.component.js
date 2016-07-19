/* global angular, console */

'use strict';

angular.
        module('jobList').
        component('jobList', {
            templateUrl: 'jsjob-list/job-list.template.html',
            controller: ['$routeParams', 'Job',
                function JobListController($routeParams, Keyword) {
                    var self = this;
                }
            ]
        });

