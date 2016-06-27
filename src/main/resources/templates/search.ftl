<#ftl strip_whitespace = false strip_text = false>
<!DOCTYPE html>
<html lang="en" ng-app="Search">
    <head>
        <#include "includes/head.ftl">
    </head>

    <body class="ng-cloak">
        <#include "includes/navbar.ftl">

        <div class="container" ng-controller="SearchController as ctrl">
            <div class="jumbotron">
                <h1 class="text-center">¡Fuder par à paz do mundo!</h1>

                <form ng-submit="submit()" name="searchForm">
                    <p>
                        <input type="text" ng-model="query" name="q" class="form-control" placeholder="What U look for?" required autofocus>
                    </p>
                    <p class="text-right">
                        <input type="submit" value="search" class="btn btn-lg btn-primary">
                    </p>
                </form>
            </div>

            <search-result ng-show="hasResult"></search-result>
        </div>

        <#include "includes/common_js.ftl">

        <script src="${baseUrl}/js/search.app.module.js"></script>
        <script src="${baseUrl}/js/search.app.config.js"></script>

        <script src="${baseUrl}/js/search-result/search-result.module.js"></script>
        <script src="${baseUrl}/js/search-result/search-result.component.js"></script>

        <script src="${baseUrl}/js/controller/search.controller.js"></script>
        <script>
            App.baseUri = '${baseUrl}';
            App.apiUri = '${apiUrl}';
            $("#search").addClass("active");
        </script>
    </body>
</html>
