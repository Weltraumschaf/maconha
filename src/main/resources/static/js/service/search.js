/* global App, console */
(function () {
    'use strict';

    App.factory('SearchService', ['$http', '$q', function ($http, $q) {
            var uri = App.apiUri + '/search';
            console.log("Register search service with base URI " + uri + ".");

            return {
                search: function (query) {
                    console.log("Search with query " + query);

                    return $http.get(uri + '/?q=' + query)
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
}());
