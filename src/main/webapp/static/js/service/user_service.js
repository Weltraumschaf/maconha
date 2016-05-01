/* global App */

'use strict';

App.factory('UserService', ['$http', '$q', function ($http, $q) {
        var uri = App.baseUr + '/user';

        return {
            fetchAllUsers: function () {
                return $http.get(uri + '/')
                        .then(
                                function (response) {
                                    return response.data;
                                },
                                function (errResponse) {
                                    console.error('Error while fetching users');
                                    return $q.reject(errResponse);
                                }
                        );
            },
            createUser: function (user) {
                return $http.post(uri + '/', user)
                        .then(
                                function (response) {
                                    return response.data;
                                },
                                function (errResponse) {
                                    console.error('Error while creating user');
                                    return $q.reject(errResponse);
                                }
                        );
            },
            updateUser: function (user, id) {
                return $http.put(uri + '/' + id, user)
                        .then(
                                function (response) {
                                    return response.data;
                                },
                                function (errResponse) {
                                    console.error('Error while updating user');
                                    return $q.reject(errResponse);
                                }
                        );
            },
            deleteUser: function (id) {
                return $http.delete(uri + '/' + id)
                        .then(
                                function (response) {
                                    return response.data;
                                },
                                function (errResponse) {
                                    console.error('Error while deleting user');
                                    return $q.reject(errResponse);
                                }
                        );
            }
        };

    }]);
