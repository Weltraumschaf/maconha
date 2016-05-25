<#ftl strip_whitespace = false strip_text = false>
<!DOCTYPE html>
<html lang="en">
    <head>
        <#include "includes/head.ftl">
    </head>

    <body ng-app="Maconha" class="ng-cloak">
        <#include "includes/navbar.ftl">

        <div class="container" ng-controller="SearchController as ctrl">
            <div class="jumbotron">
                <h1 class="text-center">¡Fuder par à paz do mundo!</h1>

                <form ng-submit="ctrl.submit()" name="searchForm">
                    <p>
                        <input type="text" ng-model="ctrl.search.query" name="q" class="form-control" placeholder="What U look for?" required autofocus>
                    </p>
                    <p class="text-right">
                        <input type="submit"  value="search" class="btn btn-lg btn-primary">
                    </p>
                </form>
            </div>

            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading">Search results</div>

                <div class="tablecontainer">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Type</th>
                                <th>title</th>
                                <th>Filename</th>
                            </tr>
                        </thead>

                        <tbody>
                            <tr ng-repeat="r in ctrl.search.result">
                                <td><span ng-bind="r.id"></span></td>
                                <td><span ng-bind="r.type"></span></td>
                                <td><span ng-bind="r.title"></span></td>
                                <td><span ng-bind="r.filename"></span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <#include "includes/common_js.ftl">
        <script src="${baseUrl}/js/service/search.js"></script>
        <script src="${baseUrl}/js/controller/search.js"></script>
        <script>
            App.baseUri = '${baseUrl}';
            App.apiUri = '${apiUrl}';
            $("#search").addClass("active");
        </script>
    </body>
</html>
