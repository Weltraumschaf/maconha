<!DOCTYPE html>
 <html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <titleヽ(°◇° )ノ Maconha ヽ(°◇° )ノ - Login</title>

        <link href="${baseUrl}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${baseUrl}/css/main.css" rel="stylesheet">
    </head>

    <body>
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
        
        <script src="${baseUrl}/js/jquery.min.js"></script>
        <script src="${baseUrl}/js/bootstrap.min.js"></script>
    </body>
</html>