const info = {
    first_name: document.forms["form"]["first_name"].value,
    last_name: document.forms["form"]["last_name"].value, 
    nationality: document.forms["form"]["nationality"].value,
    date_of_birth: document.forms["form"]["date_of_birth"].value,
    PPS_number: document.forms["form"]["PPS_number"].value,
    phone_number: document.forms["form"]["phone_number"].value,
    userEmail: document.forms["form"]["email"].value,
    password: document.forms["form"]["password"].value,
};

function Register() {
    console.log(info);
    if (checkEmail() && checkPassword()){
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/register", false)
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function () {
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
}

function checkEmail(){
    var validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    if (info.userEmail.match(validRegex) && info.userEmail.length >= 5 && info.userEmail.length <= 25) {
        return true;
    } else {
        return false;
    }
}

function checkPassword(){
    regex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8, 20}$/;
    if (info.password.match(validRegex)) {
        return true;
    } else {
        return false;
    }
}