'use strict';

angular.
        module('core.keyword').
        factory('Keyword', ['$resource',
            function ($resource) {
                return $resource('keyword/:keywordId.json', {}, {
                    query: {
                        method: 'GET',
                        params: {keywordId: 'keywords'},
                        isArray: true
                    }
                });
            }
        ]);


