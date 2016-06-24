/* global App, console */

'use strict';

angular.
    module('core.search').
    factory('Search', ['$resource', '$q',
        function ($resource, $q) {
            return $resource(App.apiUri + '/search', {}, {
                query: {
                    method: 'GET',
                    params: { $q: 'q' },
                    isArray: true
                }
            });
        }
    ]);
