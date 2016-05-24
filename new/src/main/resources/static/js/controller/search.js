/* global App */

'use strict';

App.controller('SearchController', ['$scope', 'SearchService', function ($scope, SearchService) {
        var self = this;
        self.search = {
            query: '',
            result: []
        };

        self.submit = function () {
            console.log('Search for: ', self.search.query);
            SearchService.search(self.search.query)
                    .then(
                            function (r) {
                                self.search.result = r;
                            },
                            function (errResponse) {
                                console.error('Error submit search:' + errResponse);
                            }
                    );
        };

    }]);