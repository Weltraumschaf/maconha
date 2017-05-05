
(function ($, global) {
    global.Maconha = function (baseUrl) {
        let $list = $("#result");
        let $form = $("form");
        let $error = $("#error");
        let $query = $("input[name='q']");
        let $allCheckbox = $("#all");
        let $otherCheckboxes = $("input[type='checkbox']").not("#all");
        let $numberOfResults = $("#numberOfResults");
        let $resultContainer = $("#resultContainer");

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

                let isInQuery = $.inArray(value.literal, query) > -1;

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
                $numberOfResults.text(0);
                $list.append('<li class="list-group-item">Nothing found :-(</li>');
            } else {
                $numberOfResults.text(result.length);
                $.each(result, function(index, value) {
                    let html = '<li class="list-group-item">';

                    if (value.type === 'VIDEO') {
                        html += '<span class="glyphicon glyphicon-film"></span>';
                    } else if (value.type === 'AUDIO') {
                        html += '<span class="glyphicon glyphicon-music"></span>';
                    } else {
                        html += '<span class="glyphicon glyphicon-file"></span>';
                    }

                    html += ' <strong><a href="' + baseUrl +'/files/';
                    html += value.relativeFileName +'" target="_blank">';
                    html += value.relativeFileName + '</a></strong><br>';
                    html += '<small>(' + value.bucket.directory + ')</small><br>';
                    html += 'Keywords: ' + formatKeywords(value.keywords);
                    html += '</li>';
                    $list.append(html);
                });
            }

            $resultContainer.show();
        }

        return {
            init: function () {
                $form.submit(function (event) {
                    event.preventDefault();
                    $resultContainer.hide();
                    $list.empty();
                    $numberOfResults.empty();
                    $.get(baseUrl + "/search", $form.serialize())
                        .done(showResults)
                        .fail(onError);
                });
                $allCheckbox.prop('checked', true);
                $allCheckbox.on('click', function() {
                    $otherCheckboxes.prop('checked', false);
                });
                $otherCheckboxes.on('click', function () {
                    $allCheckbox.prop('checked', false);
                })
            }
        }
    };
})(jQuery, window);