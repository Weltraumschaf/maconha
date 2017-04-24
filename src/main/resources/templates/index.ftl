<#ftl strip_whitespace = false strip_text = false>
<!DOCTYPE html>
<html lang="en">
    <head>
        <#include "includes/head.ftl">
    </head>

    <body class="ng-cloak">
        <#include "includes/navbar.ftl">

        <div class="container">
            <div class="jumbotron">
                <h1 class="text-center">¡Fuder par à paz do mundo!</h1>

                <form name="searchForm">
                    <p>
                        <input type="text" name="q" class="form-control" placeholder="What U look for?" required autofocus>
                    </p>
                    <p class="text-right">
                        <input type="submit" value="search" class="btn btn-lg btn-primary">
                    </p>
                </form>
            </div>
        </div>

        <#include "includes/common_js.ftl">

        <script>

        </script>
    </body>
</html>
