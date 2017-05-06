<#ftl strip_whitespace = false strip_text = false>
<!DOCTYPE html>
<html lang="en">
<head>
<#include "inc/head.ftl">
</head>

<body cz-shortcut-listen="true">
<#include "inc/nav.ftl">

<div class="container" role="main">
    <div class="jumbotron">
        <h1 class="text-center">Installation</h1>

    <#if installationDone>
        <p>You can go now to the <a href="${baseUrl}/admin">admin console</a> to scan your files.</p>
    <#else>
        <p>It is necessary to create the very first administrative user.</p>
    </#if>
    </div>

<#if installationDone>
    <div class="alert alert-success" role="alert">
        <p>Installation successfully done.</p>
    </div>
<#else>
    <div class="page-header">
        <h2>Create an administrative user</h2>
    </div>

    <form action="${baseUrl}/install" method="post">
        <div class="form-group">
            <label for="username">Username:
                <input type="text" class="form-control" name="username">
            </label>
        </div>

        <div class="form-group">
            <label for="username">Password:
                <input type="password" class="form-control" name="password">
            </label>
        </div>

        <button type="submit" class="btn btn-primary">Create</button>
    </form>
</#if>
</div>

<#include "inc/js.ftl">
</body>
</html>