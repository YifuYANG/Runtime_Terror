const info = {
    first_name: document.getElementById("first_name").value,
    last_name: document.getElementById("last_name").value, 
    nationality: document.getElementById("nationality").value,
    date_of_birth: document.getElementById("date_of_birth").value,
    PPS_number: document.getElementById("PPS_number").value,
    phone_number: document.getElementById("phone_number").value,
    userEmail: document.getElementById("email").value,
    password: document.getElementById("password").value,
};

function Register() {
    checkEmail();
}

function checkEmail(){
    var validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    if (info.userEmail.match(validRegex) && info.userEmail.length >= 5 && info.userEmail.length <= 25) {
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
    } else {
        alert("Invalid email address! please try again");
    }
}