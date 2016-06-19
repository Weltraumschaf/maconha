<!DOCTYPE html>
<html lang="en">
    <head>
        <#include "includes/head.ftl">
        <link href="${baseUrl}/css/admin.css" rel="stylesheet">
    </head>

    <body ng-app="Maconha" class="ng-cloak">
        <#include "includes/navbar.ftl">

        <div class="container-fluid" ng-controller="AdminController as ctrl">
            <div class="row">
                <div class="col-sm-3 col-md-2 sidebar">
                    <ul class="nav nav-sidebar">
                        <li class="active"><a href="#">Overview <span class="sr-only">(current)</span></a></li>
                        <li><a href="#">Reports</a></li>
                    </ul>
                    <ul class="nav nav-sidebar">
                        <li><a href="#" ng-click="ctrl.showScannedFiles()">Scanned files</a></li>
                        <li><a href="#" ng-click="ctrl.showImportedMedias()">Imported medias</a></li>
                        <li><a href="#" ng-click="ctrl.showKeywords()">Keywords</a></li>
                    </ul>
                    </div>
                <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                    <h1 class="page-header">Background Jobs</h1>

                    <div class="row placeholders">
                        <div class="col-xs-6 col-sm-3 placeholder">
                            <img src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" width="200" height="200" class="img-responsive" alt="Generic placeholder thumbnail">
                            <h4>Label</h4>
                            <span class="text-muted">Something else</span>
                            </div>
                        <div class="col-xs-6 col-sm-3 placeholder">
                            <img src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" width="200" height="200" class="img-responsive" alt="Generic placeholder thumbnail">
                            <h4>Label</h4>
                            <span class="text-muted">Something else</span>
                            </div>
                        <div class="col-xs-6 col-sm-3 placeholder">
                            <img src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" width="200" height="200" class="img-responsive" alt="Generic placeholder thumbnail">
                            <h4>Label</h4>
                            <span class="text-muted">Something else</span>
                            </div>
                        <div class="col-xs-6 col-sm-3 placeholder">
                            <img src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" width="200" height="200" class="img-responsive" alt="Generic placeholder thumbnail">
                            <h4>Label</h4>
                            <span class="text-muted">Something else</span>
                            </div>
                        </div>

                    <div ng-show="showScannedFiles" class="ng-show">
                        <h2 class="sub-header">Scanned files</h2>

                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <td>ID</td>
                                        <td>Base Dir</td>
                                        <td>Absolute Path</td>
                                        <td>Fingerprint</td>
                                        <td>Scan Time</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="r in ctrl.files">
                                        <td>{{r.id}}</td>
                                        <td>{{r.baseDir}}</td>
                                        <td>{{r.absolutePath}}</td>
                                        <td>{{r.fingerprint}}</td>
                                        <td>{{r.scanTime | formatDateTime}}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div ng-show="showImportedMedias" class="ng-show">
                        <h2 class="sub-header">Imported medias</h2>

                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <td>ID</td>
                                        <td>Type</td>
                                        <td>Format</td>
                                        <td>Title</td>
                                        <td>last imported</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="r in ctrl.medias">
                                        <td>{{r.id}}</td>
                                        <td>{{r.type}}</td>
                                        <td>{{r.format}}</td>
                                        <td>{{r.title}}</td>
                                        <td>{{r.lastImported | formatDateTime}}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div ng-show="showKeywords" class="ng-show">
                        <h2 class="sub-header">Keywords</h2>

                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <td>ID</td>
                                        <td>Literal</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="r in ctrl.keywords">
                                        <td>{{r.id}}</td>
                                        <td>{{r.literal}}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <#include "includes/common_js.ftl">
            <script src="${baseUrl}/js/service/admin.js"></script>
            <script src="${baseUrl}/js/controller/admin.js"></script>
            <script>
                App.baseUri = '${baseUrl}';
                App.apiUri = '${apiUrl}';
                $("#admin").addClass("active");
            </script>
        </body>
    </html>
