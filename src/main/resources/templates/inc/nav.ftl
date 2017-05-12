<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${baseUrl}">Maconha ${version}</a>
        </div>

        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="${baseUrl}/admin">Admin</a></li>
            </ul>

            <p class="navbar-text navbar-right">
                <#-- http://freemarker.org/docs/ref_builtins_number.html#ref_builtin_string_for_number -->
                Number of indexed files: ${numberOfIndexedFiles?string.number}
            </p>
        </div>
    </div>
</nav>