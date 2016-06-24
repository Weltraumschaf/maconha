/* global App, console */

'use strict';

App.controller('SearchController', ['$scope', '$http', function ($scope, $http) {
    var self = this;
    self.search = {
        query: '',
        result: []
    };

    self.submit = function () {
        $http.get(App.apiUri + '/search?q=' + self.search.query)
            .success(function (data) {
                $scope.result = data;
                self.search.result = data;
            });
    };

    self.hasResults = function () {
        return self.search.result.length > 0;
    };

}]);
