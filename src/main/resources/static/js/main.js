(function ($, global) {
    global.Maconha = function (baseUrl) {
        let $list = $("#result");
        let $form = $("form");
        let $error = $("#error");
        let $query = $("input[name='q']");

        function onError(data) {
            $error.show();
            console.error(data);
        }

        function formatKeywords(keywords) {
            let query = $query.val().toLowerCase().split(/\s+/);
            let html = '';

            $.each(keywords, function(index, value) {
                if (index > 0) {
                    html += ', ';
                }

                // FIXME Does always evaluate to false.
                let isInQuery = $.inArray(value, query) > -1;

                if (isInQuery) {
                    html += '<strong>';
                }

                html += value.literal;

                if (isInQuery) {
                    html += '</strong>';
                }
            });

            return html;
        }

        function showResults(result) {
            if (result.length === 0) {
                $list.append('<li class="list-group-item">Nothing found :-(</li>');
            } else {
                $.each(result, function(index, value) {
                    let html = '<li class="list-group-item">';

                    if (value.type === 'VIDEO') {
                        html += '<span class="glyphicon glyphicon-film"></span>'
                    } else if (value.type === 'AUDIO') {
                        html += '<span class="glyphicon glyphicon-music"></span>'
                    } else {
                        html += '<span class="glyphicon glyphicon-file"></span>'
                    }

                    html += ' <strong><a href="' + baseUrl +'/files/';
                    html += value.relativeFileName +'">'
                    html += value.relativeFileName + '</a></strong><br>';
                    html += '<small>(' + value.bucket.directory + ')</small><br>';
                    html += 'Keywords: ' + formatKeywords(value.keywords);
                    html += '</li>';
                    $list.append(html);
                });
            }

            $list.show();
        }

        return {
            init: function () {
                $form.submit(function (event) {
                    event.preventDefault();
                    $list.hide().empty();
                    $.get(baseUrl + "/search", {"q": $query.val()})
                        .done(showResults)
                        .fail(onError);
                });
            }
        }
    };
})(jQuery, window);