'use strict';

angular.
    module('core.job').
    factory('Job', ['$resource',
        function ($resource) {
            return $resource('job/:job.json', {}, {
                query: {
                    method: 'GET',
                    params: { jobId: 'jobs' },
                    isArray: true
                }
            });
        }
    ]);

