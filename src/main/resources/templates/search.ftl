<!DOCTYPE html>
<html lang="en">
    <head>
        <#include "includes/head.ftl">
    </head>

    <body ng-app="Maconha" class="ng-cloak">
        <#include "includes/navbar.ftl">

        <div class="container">
            <div class="jumbotron">
                <h1 class="text-center">¡Fuder par à paz do mundo!</h1>
                <p>
                    <input type="text" id="q" class="form-control" placeholder="What U look for?" required autofocus>
                </p>
                <p class="text-right">
                    <a class="btn btn-lg btn-primary" href="javascript:;" role="button">Search</a>
                </p>
            </div>
        </div>

        <#include "includes/common_js.ftl">
        <script src="${baseUrl}/js/service/search.js"></script>
        <script src="${baseUrl}/js/controller/search.js"></script>
    </body>
</html>
