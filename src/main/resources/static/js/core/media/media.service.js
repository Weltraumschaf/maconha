'use strict';

angular.
    module('core.media').
    factory('Media', ['$resource',
        function ($resource) {
            return $resource('media/:mediaId.json', {}, {
                query: {
                    method: 'GET',
                    params: { mediaId: 'medias' },
                    isArray: true
                }
            });
        }
    ]);

