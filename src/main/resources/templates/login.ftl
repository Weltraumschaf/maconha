<!DOCTYPE html>
<html lang="en">
    <head>
        <#include "includes/head.ftl">
    </head>

    <body>
        <#include "includes/navbar.ftl">

        <div class="container">
            <form class="form-signin" action="${baseUrl}/login" method="post">
                <h2 class="form-signin-heading">Please sign in</h2>

                <label for="username" class="sr-only">Username</label>
                <input type="text" id="username" class="form-control" placeholder="Username" required autofocus>
                <label for="password" class="sr-only">Password</label>
                <input type="password" id="password" class="form-control" placeholder="Password" required>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
            </form>
        </div>

        <#include "includes/common_js.ftl">
    </body>
</html>