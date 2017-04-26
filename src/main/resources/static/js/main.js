(function ($, global) {
    global.Maconha = function (baseUrl) {
        return {
            init: function () {
                $(function () {
                    $("form").submit(function (event) {
                        event.preventDefault();
                        // TODO Show spinner.
                        $.get(baseUrl + "/search", {"q": $("input[name='q']").val()})
                            .done(function (data) {
                                let $list = $("#result");

                                for (let item of data) {
                                    $list.append('<li class="list-group-item">' + item.relativeFileName + '</li>');
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