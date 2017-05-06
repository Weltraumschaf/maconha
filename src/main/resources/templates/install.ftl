<#ftl strip_whitespace = false strip_text = false>
<!DOCTYPE html>
<html lang="en">
<head>
<#include "inc/head.ftl">
</head>

<body>

<div class="container">
    <h1 class="text-center">Installation</h1>

<#if createAdminUser>
    <p>Create an administartiv user:</p>

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
<#else>
    <p>Nothing to do!</p>
</#if>
</div>

<#include "inc/js.ftl">
</body>
</html>