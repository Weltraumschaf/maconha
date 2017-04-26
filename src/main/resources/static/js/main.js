(function($) {

    $(function() {
        $("form").submit(function(event) {
            event.preventDefault();
            // TODO Show spinner.
            $.get("${baseUrl}/search", {"q": $("input[name='q']").val()})
            .done(function(data) {
                // TODO Show table data.
                console.log(data);
            })
            .fail(function(data) {
                $("#error").show();
                console.log(data);
            })
            .always(function(data) {
                // TODO Hide spinner.
            });
        });
    });

})(jQuery);