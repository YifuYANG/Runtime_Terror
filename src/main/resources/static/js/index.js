function indexLoading() {
    if(isLoggedIn())
        document.getElementById("loginButton").style.display = "none"
    else
        document.getElementById("loginButton").style.display = "inline"
}

function iAmAdmin() {
    if(!isLoggedIn()) alert("Please login first!")
}

function getToken() {
    return sessionStorage.getItem("token")
}

function isLoggedIn() {
    return getToken() != null;

}

function submitAppointmentForm() {

    let xhr = new XMLHttpRequest();

    if(getToken() === null) {
        alert("You need to login first!")
    }

    let data = {
        brand: document.getElementById("dose_select").value,
        date: document.getElementById("date_select").value,
        center: document.getElementById("center_select").value
    }

    xhr.open("POST", "/create-appointment",false)
    xhr.setRequestHeader("Content-Type","application/json")
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        alert(JSON.stringify(this.response))
    }
    xhr.send(JSON.stringify(data));
}

function logout() {
    if(!isLoggedIn()) {
        alert("You are not logged in.")
        return
    }
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "/sign_out",false)
    xhr.setRequestHeader("token", getToken())
    xhr.onload = function () {
        if(this.response.status === "success") {
            sessionStorage.removeItem("token")
            document.location.reload()
        } else {
            alert("Log out failed, check your token.")
        }

    }
    xhr.send();
}