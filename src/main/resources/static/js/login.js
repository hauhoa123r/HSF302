
$('#login').click((e)=>{
    e.preventDefault();
    var formLogin = $('#form-login').serializeArray();
    var json = {};
    $.each(formLogin,(i,it) => {
            json[""+it.name+""] = it.value;
    });
    loginAccount(json);
});

 function loginAccount(json) {
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
            console.log(response);
        }
    });
}