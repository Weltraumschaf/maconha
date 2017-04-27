(function ($, global) {
    global.Maconha = function (baseUrl) {
        let $list = $("#result");
        let $form = $("form");
        let $error = $("#error");

        function onError(data) {
            $error.show();
            console.error(data);
        }

        function showResults(result) {
            if (result.length === 0) {
                $list.append('<li class="list-group-item">Nothing found :-(</li>');
            } else {
                $.each(result, function(index, value) {
                    $list.append('<li class="list-group-item">' + value.relativeFileName + '</li>');
                });
            }

            $list.show();
        }

        return {
            init: function () {
                $form.submit(function (event) {
                    event.preventDefault();
                    $list.hide().empty();
                    $.get(baseUrl + "/search", {"q": $("input[name='q']").val()})
                        .done(showResults)
                        .fail(onError);
                });
            }
        }
    };
})(jQuery, window);