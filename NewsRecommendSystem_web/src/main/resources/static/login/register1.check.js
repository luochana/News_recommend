$().ready(function() {
    $("#register1Form").validate({
        debug: true,
        focusInvalid: false,
        onkeyup: false,
        submitHandler: function(form){
            form.submit();
        },
        rules: {
            name: {
                required: true,
                minlength:5,
                glossary: true,
            },
            sct:{

                required:true,
                minlength: 5,
            },
            resct:{
                required:true,
                equalTo: "#sct",
            },
        },
        messages:{
            name: {
                required: "输入不能为空",
                minlength:"最少输入5个字符",
            },
            sct: {
                required: "输入不能为空",
                minlength: "最少要5个字符",
            },
            resct:{
                required:"输入不能为空",
                equalTo: "2次密码不一致"
            },
            onsubmit:true,
            onclick:true,
        }
    })
});


jQuery.validator.addMethod("glossary",function(value,element){
    var tel=/^(([a-z])|([A-Z]))([a-z]|[A-Z]|[0-9])+$/;
    /*var te11=/[a-z]|[A-Z]/;*/
    return this.optional(element) || tel.test(value)/*||te11.test(value)*/;
},"由英文(字母开头)，数字组成");


jQuery.validator.addMethod("glossary1",function(value,element){
    var tel=/^[^u4e00-u9fa5]{0,}$/;       //匹配除了字母和数字
    /*var te11=/[a-z]|[A-Z]/;*/
    return this.optional(element) || tel.test(value)/*||te11.test(value)*/;
},"不能包含中文");