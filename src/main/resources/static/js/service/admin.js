/* global App, console */
(function () {
    'use strict';

    App.factory('AdminService', ['$http', '$q', function ($http, $q) {
            console.log("Register admin service with base URI " + App.apiUri + ".");

            return {
                allFiles: function () {
                    console.log("All files");
                    var uri = App.apiUri + '/file';
                    return $http.get(uri)
                            .then(
                                    function (response) {
                                        return response.data;
                                    },
                                    function (errResponse) {
                                        console.error('Error while searching: ' + errResponse);
                                        return $q.reject(errResponse);
                                    }
                            );
                },
                allMedias: function () {
                    console.log("All files");
                    var uri = App.apiUri + '/media';
                    return $http.get(uri)
                            .then(
                                    function (response) {
                                        return response.data;
                                    },
                                    function (errResponse) {
                                        console.error('Error while searching: ' + errResponse);
                                        return $q.reject(errResponse);
                                    }
                            );
                },
                allKeywords: function () {
                    console.log("All files");
                    var uri = App.apiUri + '/keyword';
                    return $http.get(uri)
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
