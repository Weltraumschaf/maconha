<!DOCTYPE html>
<html lang="en">
    <head>
        <#include "includes/head.ftl">
    </head>

    <body>
        <h1>Ups!</h1>

        <#list .data_model?keys as key>
            ${key}
        </#list>
    </body>
</html>