<!DOCTYPE html>
 <html lang="de">
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
        <title>ヽ(°◇° )ノ Maconha ヽ(°◇° )ノ-Access Denied</title>

        <link href="${baseUrl}/css/bootstrap.css" rel="stylesheet"/>
        <link href="${baseUrl}/css/main.css" rel="stylesheet"/>
    </head>

    <body>
        <h1>You do not have permission to access this page!</h1>

        <form action="/logout" method="post">
            <input type="submit" value="Sign in as different user" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
    </body>
</html>