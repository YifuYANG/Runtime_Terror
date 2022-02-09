function Register() {
    const info = {
        userEmail: document.getElementById("firstName").value,
        userEmail: document.getElementById("lastName").value, 
        userEmail: document.getElementById("nationality").value,
        userEmail: document.getElementById("date_of_birth").value,
        userEmail: document.getElementById("PPS_number").value,
        userEmail: document.getElementById("phone_number").value,
        userEmail: document.getElementById("email").value,
        password: document.getElementById("password").value,
    };
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