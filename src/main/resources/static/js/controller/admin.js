/* global App, console */
(function () {
    'use strict';

    App.controller('AdminController', ['$scope', 'AdminService', function ($scope, AdminService) {
            var self = this;
            self.files = [];
            self.medias = [];
            self.keywords = [];
            $scope.showScannedFiles = false;
            $scope.showImportedMedias = false;
            $scope.showKeywords = false;

            console.log("AdminController created.");

            self.showScannedFiles = function () {
                console.log("Show scanned files.");
                $scope.showScannedFiles = true;
                $scope.showImportedMedias = false;
                $scope.showKeywords = false;

                AdminService.allFiles().then(
                        function (r) {
                            console.log("Storing result: %s", r);
                            self.files = r;
                        },
                        function (errResponse) {
                            console.error('Error submit search:' + errResponse);
                        }
                );
            };

            self.showImportedMedias = function () {
                console.log("Show imported medias.");
                $scope.showScannedFiles = false;
                $scope.showImportedMedias = true;
                $scope.showKeywords = false;

                AdminService.allMedias().then(
                        function (r) {
                            console.log("Storing result: %s", r);
                            self.medias = r;
                        },
                        function (errResponse) {
                            console.error('Error submit search:' + errResponse);
                        }
                );
            };

            self.showKeywords = function () {
                console.log("Show keywords.");
                $scope.showScannedFiles = false;
                $scope.showImportedMedias = false;
                $scope.showKeywords = true;

                AdminService.allKeywords().then(
                        function (r) {
                            console.log("Storing result: %s", r);
                            self.keywords = r;
                        },
                        function (errResponse) {
                            console.error('Error submit search:' + errResponse);
                        }
                );
            };
        }]);
}());
