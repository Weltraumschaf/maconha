'use strict';

angular.
        module('core').
        filter('formatDateTime', function () {
            return function (input) {
                return input[3] + ':' + input[4] + ':' + input[5] + ' ' + input[2] + '.' + input[1] + '.' + input[0];
            };
        });

