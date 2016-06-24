/* global App, console */

'use strict';

App.controller('SearchController', ['$scope', 'Search', function ($scope, Search) {
    var self = this;
    self.search = {
        query: '',
        result: []
    };

    self.submit = function () {
        console.log('Search for: ', self.search.query);
        Search.search(self.search.query).then(
            function (r) {
                console.log("Storing result: %s", r);
                self.search.result = r;
            },
            function (errResponse) {
                console.error('Error submit search:' + errResponse);
            }
        );
    };

    self.hasResults = function () {
        return self.search.result.length > 0;
    };

}]);
