
$('login').click(()=>{
    var formLogin = $('#form-login').serializeArray();
    var json = {};
    $.each((formLogin), function(i,it){
            json[""+it.name+""] = it.value;
    });
});

function loginAccount(jdon){
    $.ajax({
        type: "POST",
        url : "/user",
        data: JSON.stringify(json),
        dataType: "json",
        contentType: "application/json",
        success : function(response){
            alert("Login Success!")
        },
        error : function(response){
            alert("Login Failed!")
        }
    });
}