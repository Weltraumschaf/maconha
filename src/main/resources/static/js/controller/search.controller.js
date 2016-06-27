/* global App, console */

'use strict';

App.controller('SearchController', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
    var self = this;
    $rootScope.result = [];

    $scope.submit = function () {
        $http.get(App.apiUri + '/search?q=' + $scope.query)
            .success(function (data) {
                console.log("Search result: %s", JSON.stringify(data));
                $rootScope.result = data;
                $scope.hasResult = data.length > 0;
            });
    };
}]);
