$().ready(function() {
    $("#loginFrom").validate({
        debug: false,
        focusInvalid: false,
        onkeyup: false,
        submitHandler: function(form){
            form.submit();
        },
        rules: {
            name: {
                required: true,
            },
            password: {
                required: true,
                minlength: 5,
            },
        },
        messages:{
               name: {
                   required: "输入不能为空",

               },
               password: {
                   required:"输入不能为空",
                   minlength: "长度不能小于5个字符",
               }
        }
    })
});