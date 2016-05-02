/* global App */

'use strict';

App.factory('AdminService', ['$http', '$q', function ($http, $q) {
        var uri = App.baseUri + '/admin';
        console.log("Register admin service with base URI " + uri + ".");

        return {

        };
}]);