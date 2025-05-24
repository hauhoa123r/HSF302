
$('login').click(()=>{
    var formLogin = $('#form-login').serializeArray();
    var json = {};
    $.each((formLogin),(i,it) => {
            json[""+it.name+""] = it.value;
    });
    loginAccount(json);
});

 function loginAccount(jdon) {
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