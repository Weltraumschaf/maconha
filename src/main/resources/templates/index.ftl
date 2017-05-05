<#ftl strip_whitespace = false strip_text = false>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>ヽ(°◇° )ノ Maconha ヽ(°◇° )ノ</title>

        <link href="${baseUrl}/lib/bootstrap/dist/css/bootstrap.css" rel="stylesheet">
        <link href="${baseUrl}/css/main.css" rel="stylesheet">
        <link href="${baseUrl}/img/favicon.ico" rel="shortcut icon" type="image/x-icon">
    </head>

    <body>
        <nav class="navbar navbar-default navbar-fixed-top">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="${baseUrl}/">Maconha</a>
                </div>

                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li id="admin"><a href="${baseUrl}/admin">Admin</a></li>
                    </ul>

                    <span class="navbar-text">Number of indexed files: #{numberOfIndexedFiles}</span>
                </div>
            </div>
        </nav>

        <div class="container">
            <div class="jumbotron">
                <h1 class="text-center">¡Fuder par à paz do mundo!</h1>

                <form action="${baseUrl}/search">
                    <p>
                        <input name="q" id="q" type="text" class="form-control" placeholder="What U look for?" required autofocus>
                    </p>
                    <p class="text-right">
                        <input name="search" id="search" type="submit" value="search" class="btn btn-lg btn-primary">
                    </p>
                    <p class="text-center">
                        <label class="checkbox-inline"><input type="checkbox" name="type[]" id="all" value="all">All</label>
                        <label class="checkbox-inline"><input type="checkbox" name="type[]" value="video">Video</label>
                        <label class="checkbox-inline"><input type="checkbox" name="type[]" value="audio">Audio</label>
                        <label class="checkbox-inline"><input type="checkbox" name="type[]" value="image">Image</label>
                        <label class="checkbox-inline"><input type="checkbox" name="type[]" value="text">Text</label>
                        <label class="checkbox-inline"><input type="checkbox" name="type[]" value="application">Application</label>
                    </p>
                </form>

                <div id="error" class="alert alert-danger fade in">
                    <a href="#" class="close" data-dismiss="alert">&times;</a>
                    <strong>Error!</strong> Can't load search results.
                </div>
            </div>
        </div>

        <div class="container" id="resultContainer">
            <p>Found <span id="numberOfResults"></span> media file.</p>
            <ul id="result" class="list-group"></ul>
        </div>

        <script src="${baseUrl}/lib/jquery/dist/jquery.js"></script>
        <script src="${baseUrl}/lib/bootstrap/dist/js/bootstrap.js"></script>
        <script src="${baseUrl}/js/main.js"></script>
        <script>
            (function ($, global) {
                $(function () {
                    new Maconha("${baseUrl}").init();
                });
            })(jQuery, window);
        </script>
    </body>
</html>
