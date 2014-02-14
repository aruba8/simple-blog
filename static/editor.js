$.ready(function () {
    $('p').hallo();


    function getText() {
        var text = $('textarea').val();
        return text;
    }

    $('input').click(function () {
        $.ajax({
            type: 'POST',
            url: '/addpost',
            data: getText(),
            success: function () {
                alert('SSSSS');
            },
            failure: function () {
                alert('FFFF');
            }

        });

    });


});
