<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>ヽ(°◇° )ノ Maconha ヽ(°◇° )ノ-Access Denied</title>

        <link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
        <link href="<c:url value='/static/css/main.css' />" rel="stylesheet"></link>
    </head>

    <body>
        <h1>You do not have permission to access this page!</h1>

        <form action="/logout" method="post">
            <input type="submit" value="Sign in as different user" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
    </body>
</html>