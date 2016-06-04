/* global App */

'use strict';

App.controller('SearchController', ['$scope', 'SearchService', function ($scope, SearchService) {
        var self = this;
        self.search = {
            query: '',
            result: []
        };

        console.log("SearchController created.");

        self.submit = function () {
            console.log('Search for: ', self.search.query);
            SearchService.search(self.search.query)
                    .then(
                            function (r) {
                                console.log("Stroing result: %s", r);
                                self.search.result = r;
                            },
                            function (errResponse) {
                                console.error('Error submit search:' + errResponse);
                            }
                    );
        };

        self.hasResults = function() {
            return self.search.result.length > 0;
        };

    }]);