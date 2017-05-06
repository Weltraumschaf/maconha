<#ftl strip_whitespace = false strip_text = false>
<!DOCTYPE html>
<html lang="en">
<head>
<#include "inc/head.ftl">
</head>

<body>
<#include "inc/nav.ftl">

<div class="container">
    <div class="jumbotron">
        <h1 class="text-center">¡Fuder par à paz do mundo!</h1>

        <form action="${baseUrl}/search">
            <div class="input-group">
                <input name="q" id="q" type="text" class="form-control" placeholder="What U look for?" required
                       autofocus>
                <span class="input-group-btn">
                    <input name="search" id="search" type="submit" value="search" class="btn btn-default">
                </span>
            </div>

            <div class="form-group text-center">
                <label class="checkbox-inline"><input type="checkbox" name="type[]" id="all" value="all">All</label>
                <label class="checkbox-inline"><input type="checkbox" name="type[]" value="video">Video</label>
                <label class="checkbox-inline"><input type="checkbox" name="type[]" value="audio">Audio</label>
                <label class="checkbox-inline"><input type="checkbox" name="type[]" value="image">Image</label>
                <label class="checkbox-inline"><input type="checkbox" name="type[]" value="text">Text</label>
                <label class="checkbox-inline"><input type="checkbox" name="type[]"
                                                      value="application">Application</label>
            </div>
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

<#include "inc/js.ftl">
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
