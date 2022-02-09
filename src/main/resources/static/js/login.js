function Login(){
    const info = {
        userEmail : document.getElementById("userEmail").value,
        password : document.getElementById("password").value,
    };
    const xhr= new XMLHttpRequest();
    xhr.open("POST", "/login",false)
    xhr.setRequestHeader("Content-Type","application/json");
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.getResponseHeader('content-type') === 'application/json') {
                var result = JSON.parse(xhr.responseText);
                if (result.status === 'success') {
                    sessionStorage.setItem("token", JSON.stringify(result.token));
                    window.location.href = "/"
                } else {
                    alert(result.msg);
                }
            }
        }
    }
    xhr.send(JSON.stringify(info));
}

function Redirect(){
    window.location.href = "/register";
}

function ValidateEmail(input) {

    var validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

    if (input.value.match(validRegex)) {
        alert("Valid email address!");
        document.form1.text1.focus();
        return true;
    } else {
        alert("Invalid email address!");
        document.form1.text1.focus();
        return false;
    }
}