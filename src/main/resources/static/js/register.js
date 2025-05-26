$('#register').click((e)=>{
    e.preventDefault();
    var formRegister = $('#form-register').serializeArray();
    var json = {};
    $.each(formRegister,(i,it) => {
        json[""+it.name+""] = it.value;
    });
    registerAccount(json);
});

function registerAccount(json) {
    $.ajax({
        type: "POST",
        url : "/user/register",
        data: JSON.stringify(json),
        dataType: "json",
        contentType: "application/json",
        success : function(response){
            alert("Register Success!")
        },
        error : function(response){
            console.log(response);
        }
    });
}