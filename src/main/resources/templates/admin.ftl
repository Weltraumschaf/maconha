<!DOCTYPE html>
<html lang="en" ng-app="Maconha">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>ヽ(°◇° )ノ Maconha ヽ(°◇° )ノ</title>

        <link href="${baseUrl}/lib/bootstrap/dist/css/bootstrap.css" rel="stylesheet">
        <link href="${baseUrl}/css/main.css" rel="stylesheet">
        <link href="${baseUrl}/css/admin.css" rel="stylesheet">
        <link href="${baseUrl}/img/favicon.ico" rel="shortcut icon" type="image/x-icon">
    </head>

    <body class="ng-cloak">
        <#include "includes/navbar.ftl">

        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-3 col-md-2 sidebar">
                    <ul class="nav nav-sidebar">
                        <li class="active"><a href="#!/overview">Overview</a></li>
                        <li><a href="#!/file">Scanned files</a></li>
                        <li><a href="#!/media">Imported medias</a></li>
                        <li><a href="#!/keyword">Keywords</a></li>
                    </ul>
                </div>

                <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                    <div ng-view></div>
                </div>
            </div>
        </div>

        <script src="${baseUrl}/lib/jquery/dist/jquery.js"></script>
        <script src="${baseUrl}/lib/bootstrap/dist/js/bootstrap.js"></script>

        <script src="${baseUrl}/lib/angular/angular.js"></script>
        <script src="${baseUrl}/lib/angular-animate/angular-animate.js"></script>
        <script src="${baseUrl}/lib/angular-resource/angular-resource.js"></script>
        <script src="${baseUrl}/lib/angular-route/angular-route.js"></script>

        <script src="${baseUrl}/js/app.module.js"></script>
        <script src="${baseUrl}/js/app.config.js"></script>
        <script src="${baseUrl}/js/core/core.module.js"></script>
        <script src="${baseUrl}/js/core/format-date-time/format-date-time.filter.js"></script>

        <script src="${baseUrl}/js/core/file/file.module.js"></script>
        <script src="${baseUrl}/js/core/file/file.service.js"></script>
        <script src="${baseUrl}/js/core/keyword/keyword.module.js"></script>
        <script src="${baseUrl}/js/core/keyword/keyword.service.js"></script>
        <script src="${baseUrl}/js/core/media/media.module.js"></script>
        <script src="${baseUrl}/js/core/media/media.service.js"></script>

        <script src="${baseUrl}/js/file-list/file-list.module.js"></script>
        <script src="${baseUrl}/js/file-list/file-list.component.js"></script>
        <script src="${baseUrl}/js/keyword-list/keyword-list.module.js"></script>
        <script src="${baseUrl}/js/keyword-list/keyword-list.component.js"></script>
        <script src="${baseUrl}/js/media-list/media-list.module.js"></script>
        <script src="${baseUrl}/js/media-list/media-list.component.js"></script>
        <script src="${baseUrl}/js/overview/overview.module.js"></script>
        <script src="${baseUrl}/js/overview/overview.component.js"></script>

        <script>
            $("#admin").addClass("active");
        </script>
    </body>
</html>
