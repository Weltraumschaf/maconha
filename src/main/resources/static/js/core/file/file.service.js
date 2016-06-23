'use strict';

angular.
        module('core.file').
        factory('File', ['$resource',
            function ($resource) {
                return $resource('file/:fileId.json', {}, {
                    query: {
                        method: 'GET',
                        params: {fileId: 'files'},
                        isArray: true
                    }
                });
            }
        ]);

