/* global App, console */
(function () {
    'use strict';

    App.factory('AdminService', ['$http', '$q', function ($http, $q) {
            console.log("Register admin service with base URI " + App.apiUri + ".");

/*
 * {"timestamp":1466344494674,"status":500,"error":"Internal Server Error","exception":"java.lang.NullPointerException"
,"message":"org.springframework.web.util.NestedServletException: Request processing failed; nested exception
 is java.lang.NullPointerException","path":"/api/file"}
 */
            function logError(e) {
                console.error('%s (%i) %s: %s',
                    e.error, e.status, e.exception, e.message);
            }

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
                                        logError(errResponse.data);
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
                                        logError(errResponse.data);
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
                                        logError(errResponse.data);
                                        return $q.reject(errResponse);
                                    }
                            );
                }
            };
        }]);
}());
