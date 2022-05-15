function Login(){
    const info = {
        userEmail : document.getElementById("userEmail").value,
        password : document.getElementById("password").value,
    };
    var validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    if(info.userEmail.match(validRegex) && info.userEmail.length>=5 && info.userEmail.length<=50){
        const xhr= new XMLHttpRequest();
        xhr.open("POST", "/login",false)
        xhr.setRequestHeader("Content-Type","application/json");
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.getResponseHeader('content-type') === 'application/json') {
                    var result = JSON.parse(xhr.responseText);
                    if (result.status === 'success') {
                        sessionStorage.setItem("token", JSON.stringify(result.token));
                        sessionStorage.setItem("role", JSON.stringify(result.role));
                        window.location.href = "/"
                    } else {
                        alert(result.msg);
                    }
                }
            }
        }
        xhr.send(JSON.stringify(info));
    } else {
        alert("Invalid email address! please try again");
    }


}

function getToken() {
    return JSON.parse(sessionStorage.getItem("token"))
}

function iAmAdmin() {
    let xhr = new XMLHttpRequest()
    xhr.open("GET", "/admin", false)
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        let newTab = window.open('/');
        newTab.focus();
        newTab.document.body.innerHTML = this.responseText;
    }
    xhr.send()
}

function Redirect(){
    window.location.href = "/register";
}
//function to get cookie from frontend
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}