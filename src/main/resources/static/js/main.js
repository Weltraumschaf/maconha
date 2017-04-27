(function ($, global) {
    global.Maconha = function (baseUrl) {
        return {
            init: function () {
                $(function () {
                    $("form").submit(function (event) {
                        event.preventDefault();
                        let $list = $("#result").hide().empty();
                        $.get(baseUrl + "/search", {"q": $("input[name='q']").val()})
                            .done(function (result) {
                                if (result.length === 0) {
                                    $list.append('<li class="list-group-item">Nothing found :-(</li>');
                                } else {
                                    $.each(result, function(index, value) {
                                        $list.append('<li class="list-group-item">' + value.relativeFileName + '</li>');
                                    });
                                }

                                $list.show();
                            })
                            .fail(function (data) {
                                $("#error").show();
                                console.log(data);
                            })
                            .always(function (data) {
                                // TODO Hide spinner.
                            });
                    });
                });
            }
        }
    };
})(jQuery, window);