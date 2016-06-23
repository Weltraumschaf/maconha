/* global angular, console */

'use strict';

angular.
        module('keywordList').
        component('keywordList', {
            templateUrl: 'js/keyword-list/keyword-list.template.html',
            controller: ['$routeParams', 'Keyword',
                function KeywordListController($routeParams, Keyword) {
                    var self = this;
                }
            ]
        });

