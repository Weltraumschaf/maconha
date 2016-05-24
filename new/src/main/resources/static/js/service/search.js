/* global App */

'use strict';

App.factory('SearchService', ['$http', '$q', function ($http, $q) {
        var uri = App.baseUri + '/search';
        console.log("Register search service with base URI " + uri + ".");

        return {
            search: function(qury) {
                return $http.get(uri + '/?q=' + qury)
                        .then(
                                function (response) {
                                    return response.data;
                                },
                                function (errResponse) {
                                    console.error('Error while searching: ' + errResponse);
                                    return $q.reject(errResponse);
                                }
                        );
            }
        };
}]);