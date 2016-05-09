<!DOCTYPE html>
<html lang="en">
    <head>
        <#include "includes/head.ftl">
    </head>

    <body ng-app="Maconha" class="ng-cloak">
        <nav class="navbar navbar-default navbar-fixed-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        </button>
                    <a class="navbar-brand" href="${baseUrl}/">Maconha</a>
                    </div>

                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li class="active"><a href="${baseUrl}/search">Search</a></li>
                        <li><a href="${baseUrl}/login">Login</a></li>
                        <li><a href="${baseUrl}/admin">Admin</a></li>
                    </ul>
                </div>
            </div>
        </nav>

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

        <script src="${baseUrl}/js/jquery.min.js"></script>
        <script src="${baseUrl}/js/bootstrap.min.js"></script>
        <script src="${baseUrl}/js/angular.js"></script>
        <script src="${baseUrl}/js/app.js"></script>
        <script src="${baseUrl}/js/service/search.js"></script>
        <script src="${baseUrl}/js/controller/search.js"></script>
    </body>
</html>
