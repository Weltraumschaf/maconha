/* global App */

'use strict';

App.factory('SearchService', ['$http', '$q', function ($http, $q) {
        var uri = App.baseUr + '/search';

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