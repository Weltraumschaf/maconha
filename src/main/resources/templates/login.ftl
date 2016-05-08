<!DOCTYPE html>
 <html lang="de">
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
        <titleヽ(°◇° )ノ Maconha ヽ(°◇° )ノ-Login</title>

        <link href="${baseUrl}/css/bootstrap.css" rel="stylesheet"/>
        <link href="${baseUrl}/css/main.css" rel="stylesheet"/>
    </head>

    <body>
        <form action="${baseUrl}/login" method="post">

            <div class="lc-block">
                <div>
                    <input type="text" class="style-4" name="username" placeholder="User Name" />
                </div>
                <div>
                    <input type="password" class="style-4" name="password" placeholder="Password" />
                </div>
                <#if error>
                    <div class="alert-danger">Invalid username and password.</div>
                </#if>
                <#if logout>                
                    <div class="alert-normal">You have been logged out.</div>
                </#if>
                <div>
                    <input type="submit" value="Sign In" class="button red small" />
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
    </body>
</html>