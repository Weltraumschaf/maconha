<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>ヽ(°◇° )ノ Maconha ヽ(°◇° )ノ</title>

        <link href="<spring:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
        <link href="<spring:url value='/static/css/main.css' />" rel="stylesheet"></link>
    </head>

    <body ng-app="Maconha" class="ng-cloak">
        <div class="generic-container" ng-controller="SearchController as ctrl">
            <div class="panel panel-default">
                <a href="<spring:url value='/admin' />">Admin</a>
                <div class="panel-heading"><span class="lead">Suche</span></div>

                <div class="formcontainer">
                    <form ng-submit="ctrl.submit()" name="searchForm" class="form-horizontal">
                        <div class="row">
                            <div class="form-group col-md-12">
                                <input type="text"
                                       ng-model="ctrl.search.query"
                                       name="query"
                                       class="username form-control input-sm"
                                       placeholder="What U look for?"
                                       required ng-minlength="3"/>
                            </div>

                            <div class="row">
                                <div class="form-actions floatRight">
                                    <input type="submit"
                                           value="search"
                                           class="btn btn-primary btn-sm"
                                           ng-disabled="searchForm.$invalid"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <div class="panel panel-default">
                <div class="panel-heading"><span class="lead">Treffer</span></div>

                <div class="tablecontainer">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Typ</th>
                                <th>Titel</th>
                                <th>Path</th>
                            </tr>
                        </thead>

                        <tbody>
                            <tr ng-repeat="r in ctrl.result">
                                <td><span ng-bind="r.id"></span></td>
                                <td><span ng-bind="r.type"></span></td>
                                <td><span ng-bind="r.title"></span></td>
                                <td><span ng-bind="r.path"></span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script src="<c:url value='/static/js/angular.js' />"></script>
        <script src="<c:url value='/static/js/app.js' />"></script>
        <script src="<c:url value='/static/js/service/search.js' />"></script>
        <script src="<c:url value='/static/js/controller/search.js' />"></script>
    </body>
</html>
