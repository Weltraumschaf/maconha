/* global angular, console */

'use strict';

angular.
        module('fileList').
        component('fileList', {
            templateUrl: 'file-list/file-list.template.html',
            controller: ['$routeParams', 'File',
                function FileListController($routeParams, File) {
                    var self = this;
                }
            ]
        });

